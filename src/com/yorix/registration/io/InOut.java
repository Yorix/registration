package com.yorix.registration.io;

import com.yorix.registration.CarriagesList;

import java.io.*;

public class InOut {

    public static CarriagesList read() {
        CarriagesList carriages;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("resources/carriagesList.dat"))) {
            carriages = (CarriagesList) ois.readObject();
        } catch (ClassNotFoundException | IOException e) {
            carriages = new CarriagesList(null);
            write(carriages);
        }
        return carriages;
    }

    public static void write(CarriagesList carriages) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("resources/carriagesList.dat"))) {
            oos.writeObject(carriages);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
