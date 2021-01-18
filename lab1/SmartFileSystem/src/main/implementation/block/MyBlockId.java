package main.implementation.block;

import main.ifs.Id;

import java.io.Serializable;

public class MyBlockId implements Id, Serializable {
    private final int id;

    public MyBlockId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
