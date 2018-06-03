package com.yorix.registration.io;

import com.yorix.registration.LorriesList;
import com.yorix.registration.Lorry;

import java.io.*;

public class InOut {

    public static Lorry[] read() {
        Lorry[] lorries;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("resources/lorries.dat"))) {
            lorries = (Lorry[]) ois.readObject();
        } catch (ClassNotFoundException | IOException e) {
            lorries = new Lorry[0];
            write(new LorriesList(0));
        }
        return lorries;
    }

    public static void write(LorriesList lorries) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("resources/lorries.dat"))) {
            oos.writeObject(lorries.getLorriesAsArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
