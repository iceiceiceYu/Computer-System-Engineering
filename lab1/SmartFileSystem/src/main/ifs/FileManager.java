package main.ifs;

public interface FileManager {
    File getFile(Id fileId) throws Exception;

    File newFile(Id fileId) throws Exception;
}
