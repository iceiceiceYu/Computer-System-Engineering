package main;

import main.exception.ErrorCode;
import main.implementation.block.MyBlockManager;
import main.implementation.file.MyFileId;
import main.implementation.file.MyFileManager;

import java.io.File;

public class SmartFileSystem {
    private static final int FILE_MANAGER_NUMBER = 3;
    private static final int BLOCK_MANAGER_NUMBER = 3;
    public final SmartTool tool;
    private final String root;
    private final char separator;
    private final MyFileManager[] fileManagers;
    private final MyBlockManager[] blockManagers;

    public SmartFileSystem() {
        String path = System.getProperty("user.dir");
        this.separator = path.startsWith("/") ? '/' : '\\';
        this.root = path + separator;
        fileManagers = new MyFileManager[FILE_MANAGER_NUMBER];
        blockManagers = new MyBlockManager[BLOCK_MANAGER_NUMBER];
        tool = new SmartTool(this);
        init();
    }

    private void init() {
        String fmRoot = root + "file" + separator;
        String bmRoot = root + "block" + separator;
        String fmPath, bmPath, dataPath, metaPath;
        File data, meta;
        MyFileManager fileManager;
        MyBlockManager blockManager;

        for (int i = 0; i < BLOCK_MANAGER_NUMBER; i++) {
            bmPath = bmRoot + "bm" + i + separator;
            dataPath = bmPath + "data" + separator;
            metaPath = bmPath + "meta" + separator;

            data = new File(dataPath);
            if (!data.exists()) {
                if (!data.mkdirs()) {
                    throw new ErrorCode(ErrorCode.SYSTEM_ERROR);
                }
            }

            meta = new File(metaPath);
            if (!meta.exists()) {
                if (!meta.mkdirs()) {
                    throw new ErrorCode(ErrorCode.SYSTEM_ERROR);
                }
            }

            blockManager = new MyBlockManager(i, bmPath);
            blockManagers[i] = blockManager;
        }

        for (int i = 0; i < FILE_MANAGER_NUMBER; i++) {
            fmPath = fmRoot + "fm" + i + separator;
            File file = new File(fmPath);
            if (!file.exists()) {
                if (!file.mkdirs()) {
                    throw new ErrorCode(ErrorCode.SYSTEM_ERROR);
                }
            }

            fileManager = new MyFileManager(i, blockManagers, fmPath);
            fileManagers[i] = fileManager;
        }
    }

    public main.ifs.File getFile(int fileManagerNumber, String fileId) {
        return fileManagers[fileManagerNumber].getFile(new MyFileId(fileId));
    }

    public main.ifs.File newFile(int fileManagerNumber, String fileId) {
        return fileManagers[fileManagerNumber].newFile(new MyFileId(fileId));
    }

    public MyFileManager[] getFileManagers() {
        return fileManagers;
    }

    public MyBlockManager[] getBlockManagers() {
        return blockManagers;
    }
}
