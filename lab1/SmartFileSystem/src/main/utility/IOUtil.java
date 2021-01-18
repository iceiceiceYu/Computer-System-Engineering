package main.utility;

import java.io.*;
import java.util.HashMap;

public class IOUtil {
    public static HashMap<String, Object> readMeta(String path) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(new File(path))));
        HashMap<String, Object> map = (HashMap<String, Object>) ois.readObject();
        ois.close();
        return map;
    }

    public static void writeMeta(String path, HashMap<String, Object> map) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(new File(path))));
        oos.writeObject(map);
        oos.close();
    }

    public static byte[] readData(String path) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(new File(path)));
        byte[] data = new byte[bis.available()];
        bis.read(data);
        bis.close();
        return data;
    }

    public static void writeData(String path, byte[] data) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(path)));
        bos.write(data);
        bos.close();
    }
}
