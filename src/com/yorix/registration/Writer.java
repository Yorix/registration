package com.yorix.registration;

import java.io.*;
import java.util.List;

public class Writer {
    private String path;

    public Writer(String path) {
        this.path = path;
    }

    public void write(List<Lorry> lorries) {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(path))) {
            objectOutputStream.writeObject(lorries);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
