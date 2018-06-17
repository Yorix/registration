package com.yorix.registration;

import com.yorix.registration.io.InOut;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.time.LocalDate;

public class CarriagesList implements Externalizable {
    private ObservableList<Carriage> carriages;
    private ObservableList<Carriage> optional;

    public CarriagesList() {
    }

    public CarriagesList(Object o) {
        carriages = FXCollections.observableArrayList();
        optional = FXCollections.observableArrayList();
    }

    public void add(Carriage carriage) {
        carriages.add(carriage);
        InOut.write(this);
    }

    public void delete(Carriage carriage) {
        carriages.remove(carriage);
        InOut.write(this);
    }

    public void setCarriages(ObservableList<Carriage> carriages) {
        this.carriages = carriages;
    }

    public ObservableList<Carriage> getCarriages() {
        return carriages;
    }

    public ObservableList<Carriage> getCarriages(LocalDate from, LocalDate to) {
        optional.clear();
        carriages.stream()
                .filter(carriage -> carriage.getDate().getDayOfYear() >= from.getDayOfYear()
                        && carriage.getDate().getDayOfYear() <= to.getDayOfYear())
                .forEach(optional::add);
        return optional;
    }

    public ObservableList<Carriage> getCarriages(Broker broker) {
        optional.clear();
        carriages.stream()
                .filter(carriage -> carriage.getBroker() == broker)
                .forEach(optional::add);
        return optional;
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
        out.writeObject(optional.toArray(new Carriage[0]));
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        carriages = FXCollections.observableArrayList((Carriage[]) in.readObject());
        optional = FXCollections.observableArrayList((Carriage[]) in.readObject());
    }
}
