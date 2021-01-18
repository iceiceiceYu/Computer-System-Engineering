package main.implementation.block;

import main.exception.ErrorCode;
import main.ifs.Block;
import main.ifs.BlockManager;
import main.ifs.Id;
import main.utility.IOUtil;
import main.utility.MD5Util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

public class MyBlock implements Block, Serializable {
    private static final int CAPACITY = 4;
    private final MyBlockId id;
    private final MyBlockManager blockManager;
    private final String root;
    private final char separator;
    private final int size;

    public MyBlock(int id, MyBlockManager blockManager, String root) {
        this.id = new MyBlockId(id);
        this.blockManager = blockManager;
        this.root = root;
        this.separator = root.charAt(root.length() - 1);
        try {
            this.size = (int) IOUtil.readMeta(root + "meta" + separator + id + ".meta").get("size");
        } catch (IOException e) {
            e.printStackTrace();
            throw new ErrorCode(ErrorCode.IO_EXCEPTION);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new ErrorCode(ErrorCode.CLASS_NOT_FOUND_EXCEPTION);
        }
    }

    public static int getCAPACITY() {
        return CAPACITY;
    }

    @Override
    public Id getIndexId() {
        return id;
    }

    @Override
    public BlockManager getBlockManager() {
        return blockManager;
    }

    @Override
    public byte[] read() {
        String metaPath = root + "meta" + separator + id.getId() + ".meta";
        String dataPath = root + "data" + separator + id.getId() + ".data";
        HashMap<String, Object> map;
        byte[] data;
        String checksum;
        try {
            map = IOUtil.readMeta(metaPath);
            data = IOUtil.readData(dataPath);
            checksum = (String) map.get("checksum");
            if (!checksum.equals(MD5Util.encrypt(data))) {
                return null;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new ErrorCode(ErrorCode.FILE_NOT_FOUND_EXCEPTION);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ErrorCode(ErrorCode.IO_EXCEPTION);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new ErrorCode(ErrorCode.CLASS_NOT_FOUND_EXCEPTION);
        }
        return data;
    }

    @Override
    public int blockSize() {
        return size;
    }
}
