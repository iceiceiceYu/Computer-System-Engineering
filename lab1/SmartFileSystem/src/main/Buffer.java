package main;

import main.implementation.block.MyBlock;

import java.util.Arrays;

public class Buffer {
    private static final int TIMES = 10;
    private static final int CAPACITY = TIMES * MyBlock.getCAPACITY();
    private final byte[] buffer;
    private int size;

    public Buffer() {
        this.buffer = new byte[0];
        this.size = 0;
    }

    public Buffer(int size) {
        this.buffer = new byte[size];
        this.size = size;
    }

    public Buffer(byte[] buffer, int size) {
        this.buffer = buffer;
        this.size = size;
    }

    public static Buffer allocate(Buffer oldBuffer, int enlargeSize) {
        return new Buffer(oldBuffer.getSize() + enlargeSize);
    }

    public static Buffer reshape(Buffer oldBuffer, int newSize) {
        byte[] buffer = Arrays.copyOf(oldBuffer.getBuffer(), newSize);
        return new Buffer(buffer, newSize);
    }

    public void copy(byte[] src, int destPos, int length) {
        System.arraycopy(src, 0, buffer, destPos, length);
    }

    public void copy(byte[] src, int srcPos, int destPos, int length) {
        System.arraycopy(src, srcPos, buffer, destPos, length);
    }

    public byte[] getBuffer() {
        return buffer;
    }

    public int getSize() {
        return size;
    }

    public boolean write(byte b) {
        if (size == buffer.length) {
            return false;
        }
        buffer[size] = b;
        size++;
        return true;
    }

    public void clear() {
        size = 0;
    }
}
