package main.implementation.file;

import main.Buffer;
import main.LogicBlock;
import main.exception.ErrorCode;
import main.ifs.File;
import main.ifs.FileManager;
import main.ifs.Id;
import main.implementation.block.MyBlock;
import main.implementation.block.MyBlockManager;
import main.utility.IOUtil;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyFile implements File, Serializable {
    private final MyFileId id;
    private final MyFileManager fileManager;
    private final String root;
    private List<LogicBlock> blocks;
    private MyBlockManager[] blockManagers;
    private Buffer buffer;
    private long size;
    private long cursor;
    private long minModifyIndex;

    public MyFile(String id, MyFileManager fileManager, MyBlockManager[] blockManagers, String root) {
        this.id = new MyFileId(id);
        this.fileManager = fileManager;
        this.blockManagers = blockManagers;
        this.root = root;
        this.blocks = new ArrayList<>();
        this.buffer = new Buffer();
        this.size = 0;
        this.cursor = 0;
        this.minModifyIndex = -1;
        save();
    }

    public MyFile(MyFileManager fileManager, String root) {
        this.fileManager = fileManager;
        this.root = root;
        this.cursor = 0;
        this.minModifyIndex = -1;
        try {
            HashMap<String, Object> map = IOUtil.readMeta(root);
            this.id = (MyFileId) map.get("id");
            this.blockManagers = (MyBlockManager[]) map.get("blockManagers");
            this.size = (long) map.get("size");
            this.blocks = (ArrayList<LogicBlock>) map.get("blocks");
            this.buffer = new Buffer((int) this.size);
            init(this.buffer, this.blocks);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ErrorCode(ErrorCode.IO_EXCEPTION);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new ErrorCode(ErrorCode.CLASS_NOT_FOUND_EXCEPTION);
        }
    }

    @Override
    public Id getFileId() {
        return id;
    }

    @Override
    public FileManager getFileManager() {
        return fileManager;
    }

    @Override
    public byte[] read(int length) {
        if (length < 0) {
            ErrorCode.ErrorCodeHandler(ErrorCode.INVALID_ARGUMENT);
            return null;
        }
        if (length == 0) {
            return new byte[0];
        }
        if (cursor + length > size) {
            ErrorCode.ErrorCodeHandler(ErrorCode.END_OF_FILE);
            return null;
        }
        byte[] data = new byte[length];
        System.arraycopy(buffer.getBuffer(), (int) cursor, data, 0, length);
        move(length, MOVE_CURR);
        return data;
    }

    @Override
    public void write(byte[] b) {
        Buffer newBuffer = Buffer.allocate(buffer, b.length);
        byte[] oldBuffer = buffer.getBuffer();
        int oldBufferSize = buffer.getSize();
        if (cursor == 0) {
            newBuffer.copy(b, 0, b.length);
            newBuffer.copy(oldBuffer, b.length, oldBufferSize);
        } else if (cursor == size) {
            newBuffer.copy(oldBuffer, 0, oldBufferSize);
            newBuffer.copy(b, oldBufferSize, b.length);
        } else {
            newBuffer.copy(oldBuffer, 0, (int) cursor);
            newBuffer.copy(b, (int) cursor, b.length);
            newBuffer.copy(oldBuffer, (int) cursor, (int) (cursor + b.length), (int) (oldBufferSize - cursor));
        }
        if (minModifyIndex == -1) {
            minModifyIndex = cursor;
        } else if (cursor < minModifyIndex) {
            minModifyIndex = cursor;
        }
        size = size + b.length;
        buffer = newBuffer;
        move(b.length, MOVE_CURR);
    }

    @Override
    public long move(long offset, int where) {
        long newCursor;
        switch (where) {
            case MOVE_CURR:
                newCursor = cursor + offset;
                break;
            case MOVE_HEAD:
                newCursor = offset;
                break;
            case MOVE_TAIL:
                newCursor = size + offset;
                break;
            default:
                newCursor = -1;
        }
        if (newCursor >= 0 && newCursor <= size) {
            cursor = newCursor;
            return cursor;
        }
        ErrorCode.ErrorCodeHandler(ErrorCode.INVALID_CURSOR_MOVE);
        return -1;
    }

    @Override
    public void close() {
        flush();
    }

    @Override
    public long size() {
        return size;
    }

    @Override
    public void setSize(long newSize) {
        Buffer newBuffer = Buffer.reshape(buffer, (int) newSize);
        if (newSize > size) {
            if (minModifyIndex == -1) {
                minModifyIndex = size;
            }
        } else if (newSize < size) {
            if (minModifyIndex == -1) {
                minModifyIndex = (newSize / MyBlock.getCAPACITY()) * MyBlock.getCAPACITY();
            } else if ((newSize / MyBlock.getCAPACITY()) * MyBlock.getCAPACITY() < minModifyIndex) {
                minModifyIndex = (newSize / MyBlock.getCAPACITY()) * MyBlock.getCAPACITY();
            }
            move(0, MOVE_HEAD);
        }
        size = newSize;
        buffer = newBuffer;
    }

    public void copyFile(HashMap<String, Object> map) {
        blockManagers = (MyBlockManager[]) map.get("blockManagers");
        size = (long) map.get("size");
        blocks = (ArrayList<LogicBlock>) map.get("blocks");
        buffer = new Buffer((int) this.size);
        init(buffer, blocks);
        try {
            IOUtil.writeMeta(root, map);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ErrorCode(ErrorCode.IO_EXCEPTION);
        }
    }

    private void save() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("blockManagers", blockManagers);
        map.put("size", size);
        map.put("blocks", blocks);
        try {
            IOUtil.writeMeta(root, map);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ErrorCode(ErrorCode.IO_EXCEPTION);
        }
    }

    private void init(Buffer buffer, List<LogicBlock> blocks) {
        byte[] data;
        int count = 0;
        for (LogicBlock block : blocks) {
            data = block.read();
            if (data != null) {
                buffer.copy(data, count * MyBlock.getCAPACITY(), data.length);
            }
            count++;
        }
    }

    private void flush() {
        if (minModifyIndex != -1) {
            int minModifyBlock = (int) (minModifyIndex / MyBlock.getCAPACITY());
            int times = (int) Math.ceil((double) size / MyBlock.getCAPACITY());
            byte[] bufferBytes = buffer.getBuffer();
            int length;

            if (blocks.size() > minModifyBlock) {
                blocks.subList(minModifyBlock, blocks.size()).clear();
            }

            long totalLength = size - minModifyBlock * MyBlock.getCAPACITY();

            for (int i = minModifyBlock; i < times; i++) {
                length = Math.min(MyBlock.getCAPACITY(), (int) totalLength);
                byte[] data = new byte[length];
                System.arraycopy(bufferBytes, i * MyBlock.getCAPACITY(), data, 0, length);
                LogicBlock block = new LogicBlock(blockManagers, data);
                blocks.add(block);
                totalLength = totalLength - length;
            }
        }
        save();
    }

    public String getRoot() {
        return root;
    }
}
