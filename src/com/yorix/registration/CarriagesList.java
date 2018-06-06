package com.yorix.registration;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;

public class CarriagesList implements Externalizable {
    private ObservableList<Carriage> carriages;

    public CarriagesList() {
    }

    public CarriagesList(Lorry lorry) {
        carriages = FXCollections.observableArrayList();
    }

    public void add(Lorry lorry, Carriage carriage) {
        carriages.add(carriage);
        lorry.setBroker();
    }

    public void delete(Lorry lorry) {

    }

    public void setCarriages(ObservableList<Carriage> carriages) {
        this.carriages = carriages;
    }

    public ObservableList<Carriage> getCarriages() {
        return carriages;
    }

    public Carriage[] getCarriagesAsArray() {
        return carriages.toArray(new Carriage[0]);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Carriage carriage : carriages) {
            builder.append(carriage).append("\n");
        }
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(carriages.toArray(new Carriage[0]));
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        carriages = FXCollections.observableArrayList((Carriage[]) in.readObject());
    }
}
