package main;

import main.exception.ErrorCode;
import main.ifs.Id;
import main.implementation.block.MyBlock;
import main.implementation.block.MyBlockId;
import main.implementation.block.MyBlockManager;
import main.implementation.file.MyFile;
import main.implementation.file.MyFileId;
import main.implementation.file.MyFileManager;

import java.util.Arrays;
import java.util.Scanner;

public class Application {
    public static void main(String[] args) {
        SmartFileSystem sfs = new SmartFileSystem();
        welcome();
        Scanner input = new Scanner(System.in);
        String instruction;
        MyFile file = null;
        MyFile newFile;
        while (true) {
            if (file != null) {
                System.out.print("\nOperation in file: " + ((MyFileId) file.getFileId()).getId());
                System.out.println(", in file manager " + ((MyFileManager) file.getFileManager()).getId().getId());
            } else {
                System.out.println("\nNo file opening now.");
            }
            System.out.print(System.getProperty("user.dir") + ": ");
            System.out.print(">>> ");
            instruction = input.nextLine();
            newFile = operate(instruction, sfs, file);
            if (newFile != null) {
                file = newFile;
                if (instruction.equals("sfs close")) {
                    file = null;
                }
            }
        }
    }

    private static void welcome() {
        System.out.println("Welcome to Smart File System!");
        print();
    }

    private static MyFile operate(String instruction, SmartFileSystem sfs, MyFile file) {
        String[] commands = instruction.split(" ");
        MyFile newFile;
        switch (commands[0]) {
            case "list":
                list(sfs);
                return null;
            case "usage":
                usage();
                return null;
            case "quit":
                exit();
            case "sfs":
                newFile = execute(instruction.substring(instruction.indexOf(' ') + 1), sfs, file);
                return newFile;
            default:
                System.out.println("Wrong usage!");
                print();
                return null;
        }
    }

    private static void print() {
        System.out.println("Please enter \"list\" to see all the files in this system.");
        System.out.println("Please enter \"usage\" to see how to use this system.");
        System.out.println("Please enter \"quit\" to quit this system.");
    }

    private static void list(SmartFileSystem sfs) {
        MyFileManager[] fileManagers = sfs.getFileManagers();
        System.out.println("Total file manger number: " + fileManagers.length);
        for (MyFileManager fileManager : fileManagers) {
            fileManager.listFile();
        }
    }

    private static void usage() {
        System.out.println("To create a new file: [sfs] [new] [file manager number] [file name]");
        System.out.println("To get an existed file: [sfs] [get] [file manager number] [file name]");
        System.out.println("To read file data: [sfs] [read] [length]");
        System.out.println("To write file data: [sfs] [write] [what you want to write]");
        System.out.println("To move cursor: [sfs] [move] [offset] [where]");
        System.out.println("To get file size: [sfs] [size]");
        System.out.println("To reset file size: [sfs] [set] [new size]");
        System.out.println("To close and save file: [sfs] [close]");
        System.out.println("To use smart_cat: [sfs] [smart_cat] [file manager number] [file name]");
        System.out.println("To use smart_hex: [sfs] [smart_hex] [block manager number] [block index]");
        System.out.println("To use smart_write: [sfs] [smart_write] [file manager number] [file name] [index]");
        System.out.println("To use smart_copy: [sfs] [smart_copy] [from file manager number] [from file name] [to file manager number] [to file name]");
    }

    private static void exit() {
        System.out.println("Bye-bye!");
        System.exit(0);
    }

    private static MyFile execute(String instruction, SmartFileSystem sfs, MyFile file) {
        String[] commands = instruction.split(" ");
        MyFileManager[] fileManagers = sfs.getFileManagers();
        MyBlockManager[] blockManagers = sfs.getBlockManagers();
        switch (commands[0]) {
            case "new":
                if (Integer.parseInt(commands[1]) > -1 && Integer.parseInt(commands[1]) < fileManagers.length) {
                    Id fileId = new MyFileId(commands[2]);
                    return (MyFile) fileManagers[Integer.parseInt(commands[1])].newFile(fileId);
                } else {
                    ErrorCode.ErrorCodeHandler(ErrorCode.MANAGER_NOT_EXIST);
                    return null;
                }
            case "get":
                if (Integer.parseInt(commands[1]) > -1 && Integer.parseInt(commands[1]) < fileManagers.length) {
                    Id fileId = new MyFileId(commands[2]);
                    return (MyFile) fileManagers[Integer.parseInt(commands[1])].getFile(fileId);
                } else {
                    ErrorCode.ErrorCodeHandler(ErrorCode.MANAGER_NOT_EXIST);
                    return null;
                }
            case "read":
                if (file != null) {
                    byte[] data = file.read(Integer.parseInt(commands[1]));
                    if (data != null) {
                        System.out.println("file data (byte[]) is: " + Arrays.toString(data));
                        System.out.println("file data is: " + new String(data));
                    }
                    return file;
                } else {
                    ErrorCode.ErrorCodeHandler(ErrorCode.NOT_OPERATING_FILE_NOW);
                    return null;
                }
            case "write":
                if (file != null) {
                    instruction = instruction.substring(instruction.indexOf(' ') + 1);
                    file.write(instruction.getBytes());
                    return file;
                } else {
                    ErrorCode.ErrorCodeHandler(ErrorCode.NOT_OPERATING_FILE_NOW);
                    return null;
                }
            case "move":
                if (file != null) {
                    file.move(Long.parseLong(commands[1]), Integer.parseInt(commands[2]));
                    return file;
                } else {
                    ErrorCode.ErrorCodeHandler(ErrorCode.NOT_OPERATING_FILE_NOW);
                    return null;
                }
            case "size":
                if (file != null) {
                    System.out.println("file size is: " + file.size());
                    return file;
                } else {
                    ErrorCode.ErrorCodeHandler(ErrorCode.NOT_OPERATING_FILE_NOW);
                    return null;
                }
            case "set":
                if (file != null) {
                    file.setSize(Long.parseLong(commands[1]));
                    return file;
                } else {
                    ErrorCode.ErrorCodeHandler(ErrorCode.NOT_OPERATING_FILE_NOW);
                    return null;
                }
            case "close":
                if (file != null) {
                    file.close();
                    return file;
                } else {
                    ErrorCode.ErrorCodeHandler(ErrorCode.NOT_OPERATING_FILE_NOW);
                    return null;
                }
            case "smart_cat":
                if (Integer.parseInt(commands[1]) > -1 && Integer.parseInt(commands[1]) < fileManagers.length) {
                    sfs.tool.smart_cat(Integer.parseInt(commands[1]), commands[2]);
                } else {
                    ErrorCode.ErrorCodeHandler(ErrorCode.MANAGER_NOT_EXIST);
                }
                return null;
            case "smart_hex":
                if (Integer.parseInt(commands[1]) > -1 && Integer.parseInt(commands[1]) < blockManagers.length) {
                    Id indexId = new MyBlockId(Integer.parseInt(commands[2]));
                    MyBlock block = (MyBlock) blockManagers[Integer.parseInt(commands[1])].getBlock(indexId);
                    sfs.tool.smart_hex(block);
                } else {
                    ErrorCode.ErrorCodeHandler(ErrorCode.MANAGER_NOT_EXIST);
                }
                return null;
            case "smart_write":
                if (Integer.parseInt(commands[1]) > -1 && Integer.parseInt(commands[1]) < fileManagers.length) {
                    sfs.tool.smart_write(Integer.parseInt(commands[1]), commands[2], Integer.parseInt(commands[3]));
                } else {
                    ErrorCode.ErrorCodeHandler(ErrorCode.MANAGER_NOT_EXIST);
                }
                return null;
            case "smart_copy":
                if (Integer.parseInt(commands[1]) > -1 && Integer.parseInt(commands[1]) < fileManagers.length
                        && Integer.parseInt(commands[3]) > -1 && Integer.parseInt(commands[3]) < fileManagers.length) {
                    sfs.tool.smart_copy(Integer.parseInt(commands[1]), commands[2], Integer.parseInt(commands[3]), commands[4]);
                } else {
                    ErrorCode.ErrorCodeHandler(ErrorCode.MANAGER_NOT_EXIST);
                }
                return null;
            default:
                System.out.println("Wrong usage!");
                usage();
                return null;
        }
    }
}
