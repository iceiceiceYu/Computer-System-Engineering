package main.implementation.block;

import main.exception.ErrorCode;
import main.ifs.Block;
import main.ifs.BlockManager;
import main.ifs.Id;
import main.utility.IOUtil;
import main.utility.MD5Util;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyBlockManager implements BlockManager, Serializable {
    private final MyBlockManagerId id;
    private final String root;
    private final char separator;
    private final List<MyBlock> blocks;

    public MyBlockManager(int id, String root) {
        this.id = new MyBlockManagerId(id);
        this.root = root;
        this.separator = root.charAt(root.length() - 1);
        this.blocks = new ArrayList<>();
        init();
    }

    @Override
    public Block getBlock(Id indexId) {
        int id = ((MyBlockId) indexId).getId();
        for (MyBlock block : blocks) {
            if (((MyBlockId) block.getIndexId()).getId() == id) {
                return block;
            }
        }
        ErrorCode.ErrorCodeHandler(ErrorCode.BLOCK_NOT_FOUND);
        return null;
    }

    @Override
    public Block newBlock(byte[] b) {
        MyBlock newBlock;
        String metaPath = root + "meta" + separator + blocks.size() + ".meta";
        String dataPath = root + "data" + separator + blocks.size() + ".data";

        // todo: delete
        System.out.println("metaPath:" + metaPath);
        System.out.println("dataPath:" + dataPath);

        HashMap<String, Object> map = new HashMap<>();
        map.put("checksum", MD5Util.encrypt(b));
        map.put("size", b.length);

        try {
            IOUtil.writeData(dataPath, b);
            IOUtil.writeMeta(metaPath, map);
            newBlock = new MyBlock(blocks.size(), this, root);
            blocks.add(newBlock);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ErrorCode(ErrorCode.IO_EXCEPTION);
        }
        return newBlock;
    }

    private void init() {
        File blockManager = new File(root + "data" + separator);
        File[] children = blockManager.listFiles();
        if (children != null) {
            String fileName;
            MyBlock block;
            for (File child : children) {
                fileName = child.getName();
                if (fileName.charAt(0) == '.') {
                    continue;
                }
                block = new MyBlock(Integer.parseInt(fileName.substring(0, fileName.lastIndexOf('.'))), this, root);
                blocks.add(block);
            }
        }
    }
}
