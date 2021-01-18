package main.ifs;

public interface BlockManager {
    Block getBlock(Id indexId) throws Exception;

    Block newBlock(byte[] b);

    default Block newEmptyBlock(int blockSize) {
        return newBlock(new byte[blockSize]);
    }
}
