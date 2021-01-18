package main.implementation.block;

import main.ifs.Id;

import java.io.Serializable;

public class MyBlockManagerId implements Id, Serializable {
    private final int id;

    public MyBlockManagerId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
