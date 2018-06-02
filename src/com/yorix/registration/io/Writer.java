package com.yorix.registration.io;

import com.yorix.registration.Lorry;
import javafx.collections.ObservableList;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Arrays;

public class Writer {
    public static void write(ObservableList<Lorry> lorries) {
        try (FileOutputStream fos = new FileOutputStream("resources/lorries.dat");
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(Arrays.asList(lorries.toArray()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
