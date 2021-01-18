package main.implementation.file;

import main.ifs.Id;

import java.io.Serializable;

public class MyFileManagerId implements Id, Serializable {
    private final int id;

    public MyFileManagerId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
