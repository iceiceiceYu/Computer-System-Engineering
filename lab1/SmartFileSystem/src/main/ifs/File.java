package main.ifs;

public interface File {
    int MOVE_CURR = 0;
    int MOVE_HEAD = 1;
    int MOVE_TAIL = 2;

    Id getFileId();

    FileManager getFileManager();

    byte[] read(int length) throws Exception;

    void write(byte[] b) throws Exception;

    default long pos() {
        return move(0, MOVE_CURR);
    }

    long move(long offset, int where);

    // 使用buffer的同学需要实现
    void close() throws Exception;

    long size();

    void setSize(long newSize);
}
