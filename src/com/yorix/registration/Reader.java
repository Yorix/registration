package com.yorix.registration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Collections;
import java.util.List;

public class Reader {
    private String path;

    public Reader(String path) {
        this.path = path;
    }

    public List<Lorry> read() {
        List<Lorry> lorries = Collections.emptyList();

        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(path))) {
            lorries = (List<Lorry>) objectInputStream.readObject();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return lorries;
    }
}
