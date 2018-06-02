package com.yorix.registration.io;

import com.yorix.registration.Lorry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

public class Reader {
    public static ObservableList<Lorry> read() {
        ObservableList lorries = FXCollections.observableArrayList();
        try (FileInputStream fis = new FileInputStream("resources/lorries.dat");
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            lorries = FXCollections.observableArrayList(ois.readObject());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return lorries;
    }
}
