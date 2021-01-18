package main.implementation.file;

import main.ifs.Id;

import java.io.Serializable;

public class MyFileId implements Id, Serializable {
    private final String id;

    public MyFileId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
