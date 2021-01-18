package main;

import main.exception.ErrorCode;
import main.implementation.block.MyBlock;
import main.implementation.block.MyBlockManager;

import java.io.Serializable;
import java.util.Random;

public class LogicBlock implements Serializable {
    private static final int CAPACITY = MyBlock.getCAPACITY();
    private static final int LOGIC_BLOCK_NUMBER = 2;
    private final MyBlock[] blocks;

    public LogicBlock(MyBlockManager[] blockManagers, byte[] data) {
        this.blocks = new MyBlock[LOGIC_BLOCK_NUMBER];
        init(blockManagers, data);
    }

    public byte[] read() {
        byte[] data;
        for (MyBlock block : blocks) {
            data = block.read();
            if (data != null) {
                return data;
            }
        }
        ErrorCode.ErrorCodeHandler(ErrorCode.CHECKSUM_CHECK_FAILED);
        return null;
    }

    public void write(MyBlockManager[] blockManagers, byte[] data) {
        for (int i = 0; i < LOGIC_BLOCK_NUMBER; i++) {
            blocks[i] = null;
        }
        init(blockManagers, data);
    }

    private void init(MyBlockManager[] blockManagers, byte[] data) {
        for (int i = 0; i < LOGIC_BLOCK_NUMBER; i++) {
            MyBlockManager blockManager = blockManagers[new Random().nextInt(blockManagers.length)];
            blocks[i] = (MyBlock) blockManager.newBlock(data);
        }
    }
}
