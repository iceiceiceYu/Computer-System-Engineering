package main.implementation.file;

import main.exception.ErrorCode;
import main.ifs.File;
import main.ifs.FileManager;
import main.ifs.Id;
import main.implementation.block.MyBlockManager;
import main.utility.IOUtil;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyFileManager implements FileManager, Serializable {
    private final MyFileManagerId id;
    private final MyBlockManager[] blockManagers;
    private final String root;
    private final char separator;
    private final List<MyFile> files;

    public MyFileManager(int id, MyBlockManager[] blockManagers, String root) {
        this.id = new MyFileManagerId(id);
        this.blockManagers = blockManagers;
        this.root = root;
        this.separator = root.charAt(root.length() - 1);
        files = new ArrayList<>();
        init();
    }

    public MyFileManagerId getId() {
        return id;
    }

    @Override
    public File getFile(Id fileId) {
        String id = ((MyFileId) fileId).getId();
        for (MyFile file : files) {
            if (((MyFileId) file.getFileId()).getId().equals(id)) {
                return file;
            }
        }
        ErrorCode.ErrorCodeHandler(ErrorCode.FILE_NOT_FOUND);
        return null;
    }

    @Override
    public File newFile(Id fileId) {
        String id = ((MyFileId) fileId).getId();
        for (MyFile file : files) {
            if (((MyFileId) file.getFileId()).getId().equals(id)) {
                ErrorCode.ErrorCodeHandler(ErrorCode.FILE_ALREADY_EXIST);
                return null;
            }
        }
        String path = root + id;
        MyFile newFile = new MyFile(id, this, blockManagers, path);
        files.add(newFile);
        return newFile;
    }

    public void listFile() {
        HashMap<String, Object> map;
        MyFileId fileId;
        long size;
        System.out.println("In file manager " + id.getId() + ", Total files: " + files.size());
        for (MyFile file : files) {
            try {
                map = IOUtil.readMeta(file.getRoot());
                fileId = (MyFileId) map.get("id");
                size = (long) map.get("size");
                System.out.println("file name: " + fileId.getId() + ", size: " + size);
            } catch (IOException e) {
                e.printStackTrace();
                throw new ErrorCode(ErrorCode.IO_EXCEPTION);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                throw new ErrorCode(ErrorCode.CLASS_NOT_FOUND_EXCEPTION);
            }
        }
    }

    private void init() {
        java.io.File fileManager = new java.io.File(root);
        java.io.File[] children = fileManager.listFiles();
        if (children != null) {
            MyFile file;
            for (java.io.File child : children) {
                if (child.getName().charAt(0) == '.') {
                    continue;
                }
                file = new MyFile(this, child.getPath());
                files.add(file);
            }
        }
    }
}
