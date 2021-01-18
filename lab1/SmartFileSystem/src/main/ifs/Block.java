package main.ifs;

import java.io.FileNotFoundException;

public interface Block {
    Id getIndexId();

    BlockManager getBlockManager();

    byte[] read() throws FileNotFoundException;

    int blockSize();
}
