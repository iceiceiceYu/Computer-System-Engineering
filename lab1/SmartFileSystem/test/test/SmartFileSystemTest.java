package test;

//import main.SmartFileSystem;
//import main.ifs.File;
//import main.implementation.file.MyFile;
//
//import java.util.Arrays;
//
//class SmartFileSystemTest {
//    public static void main(String[] args) {
//        SmartFileSystem sfs = new SmartFileSystem();
//        MyFile test1 = (MyFile) sfs.newFile(0, "test1");
//        MyFile file = (MyFile) sfs.getFile(0, "test1");
//        byte[] bytes = new byte[10];
//        for (int i = 0; i < bytes.length; i++)
//            bytes[i] = (byte) (i + 20);
//        file.write(bytes);
//        file.move(0, File.MOVE_HEAD);
//        System.out.println(Arrays.toString(file.read((int) file.size())));
//        file.close();
//
//
//        MyFile test2 = (MyFile) sfs.newFile(1, "test2");
//        MyFile test3 = (MyFile) sfs.newFile(2, "test3");
//        MyFile test4 = (MyFile) sfs.newFile(0, "test2");
//
//
//        SmartFileSystem sfs = new SmartFileSystem();
//
//        MyFile file = (MyFile) sfs.newFile(0, "test");
//        MyFile file = (MyFile) sfs.getFile(0, "test");
//
//        Id indexId = new MyBlockId(0);
//        MyBlock block = (MyBlock) (sfs.getBlockManagers())[2].getBlock(indexId);
//        sfs.tool.smart_hex((MyBlock) (sfs.getBlockManagers())[2].getBlock(indexId));
//
//        file.move(0, File.MOVE_HEAD);
//        System.out.println(Arrays.toString(file.read((int) file.size())));
//        file.close();
//        sfs.tool.smart_copy("test", "copy", 0, 1);
//        MyFile file1 = (MyFile) sfs.getFile(1, "copy");
//        //file1.move(0, File.MOVE_HEAD);
//        System.out.println(Arrays.toString(file1.read(10)));
//        System.out.println("--------");
//        file1.close();
//
//
//        MyFile file = (MyFile) sfs.getFile(1, "copy");
//        sfs.tool.smart_cat("copy", 1);
//        file.move(0, File.MOVE_HEAD);
//        System.out.println(Arrays.toString(file.read((int) file.size())));
//        sfs.tool.smart_write("copy", 10, 1);
//        file.move(0, File.MOVE_HEAD);
//        System.out.println(Arrays.toString(file.read((int) file.size())));
//        file.close();
//        System.out.println(file.size());
//        file.setSize(16);
//        file.move(0, MyFile.MOVE_HEAD);
//        System.out.println(Arrays.toString(file.read(16)));
//
//        file.move(0, MyFile.MOVE_TAIL);
//        byte[] bytes = new byte[3];
//        for (int i = 0; i < bytes.length; i++)
//            bytes[i] = (byte) i;
//        file.write(bytes);
//        file.move(0, MyFile.MOVE_HEAD);
//        System.out.println(Arrays.toString(file.read(11)));
//        file.move(0, MyFile.MOVE_HEAD);
//        System.out.println(Arrays.toString(file.read(12)));
//        file.move(0, MyFile.MOVE_HEAD);
//        System.out.println(Arrays.toString(file.read(13)));
//        file.close();
//    }
//}
