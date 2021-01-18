package main;

import main.exception.ErrorCode;
import main.ifs.File;
import main.ifs.Id;
import main.implementation.block.MyBlock;
import main.implementation.file.MyFile;
import main.implementation.file.MyFileId;
import main.implementation.file.MyFileManager;
import main.utility.IOUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class SmartTool {
    private final MyFileManager[] fileManagers;

    public SmartTool(SmartFileSystem sfs) {
        this.fileManagers = sfs.getFileManagers();
    }

    public void smart_cat(int fileManagerNumber, String fileName) {
        Id fileId = new MyFileId(fileName);
        MyFileManager fileManager = fileManagers[fileManagerNumber];
        MyFile file = (MyFile) fileManager.getFile(fileId);
        if (file != null) {
            file.move(0, File.MOVE_HEAD);
            byte[] data = file.read((int) file.size());
            System.out.println("Successfully read!");
            System.out.println("file data (byte[]) is: " + Arrays.toString(data));
            System.out.println("file data is: " + new String(data));
        }
    }

    public void smart_hex(MyBlock block) {
        byte[] data = block.read();
        if (data != null) {
            StringBuilder sb = new StringBuilder();
            int v;
            String hv;
            for (byte b : data) {
                v = b & 0xff;
                hv = Integer.toHexString(v);
                if (hv.length() < 2) {
                    sb.append(0);
                }
                sb.append(hv);
            }
            System.out.println("Successfully changed byte[] to hex!");
            System.out.println(sb.toString() + "");
        } else {
            System.out.println("Checksum check failed and there may be some wrong with the block data.");
            System.out.println("Failed to change byte[] to hex!");
        }
    }

    public void smart_write(int fileManagerNumber, String fileName, int index) {
        Id fileId = new MyFileId(fileName);
        MyFileManager fileManager = fileManagers[fileManagerNumber];
        MyFile file = (MyFile) fileManager.getFile(fileId);
        if (file != null) {
            if (file.move(index, File.MOVE_HEAD) != -1) {
                System.out.println("Please enter your input data:");
                System.out.print(">>> ");
                Scanner input = new Scanner(System.in);
                String str = input.nextLine();
                byte[] data = str.getBytes();
                file.write(data);
                System.out.println("Successfully written!");
            }
        }
    }

    public void smart_copy(int fromFileManagerNumber, String from, int toFileManagerNumber, String to) {
        Id fromFileId = new MyFileId(from);
        Id toFileId = new MyFileId(to);
        MyFileManager fromFileManager = fileManagers[fromFileManagerNumber];
        MyFileManager toFileManager = fileManagers[toFileManagerNumber];
        MyFile fromFile = (MyFile) fromFileManager.getFile(fromFileId);
        if (fromFile != null) {
            try {
                HashMap<String, Object> map = IOUtil.readMeta(fromFile.getRoot());
                MyFile toFile = (MyFile) toFileManager.getFile(toFileId);
                if (toFile == null) {
                    System.out.println("File system is automatically creating destination file.");
                    toFile = (MyFile) toFileManager.newFile(toFileId);
                    System.out.println("Destination file is automatically created by file system.");
                }
                toFile.copyFile(map);
                System.out.println("Successfully copied!");
            } catch (IOException e) {
                e.printStackTrace();
                throw new ErrorCode(ErrorCode.IO_EXCEPTION);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                throw new ErrorCode(ErrorCode.CLASS_NOT_FOUND_EXCEPTION);
            }
        }
    }
}
