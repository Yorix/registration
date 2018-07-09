package com.yorix.registration;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.time.LocalDate;

public class CarriagesList implements Externalizable {
    private static final long serialVersionUID = -8180360097410716153L;
    private ObservableList<Carriage> carriages;
    private ObservableList<Carriage> optional;
    private Broker currentBrocker;


    public CarriagesList() {
    }

    public CarriagesList(boolean createEmptyList) {
        if (createEmptyList) {
            carriages = FXCollections.observableArrayList();
            optional = FXCollections.observableArrayList();
        }
    }

    public void add(Carriage carriage) {
        carriages.add(carriage);
        if (carriage.getBroker() == currentBrocker || currentBrocker == null) optional.add(carriage);
    }

    public void delete(Carriage carriage) {
        carriages.remove(carriage);
        optional.remove(carriage);
    }

    public void setCurrentBrocker(Broker currentBrocker) {
        this.currentBrocker = currentBrocker;
    }

    public Broker getCurrentBrocker() {
        return currentBrocker;
    }

    public ObservableList<Carriage> getCarriages() {
        return carriages;
    }

    public ObservableList<Carriage> getOptionalList(LocalDate from, LocalDate to, Broker broker) {
        if (optional == null) optional = FXCollections.observableArrayList();
        optional.clear();
        carriages.stream()
                .filter(carriage -> carriage.getBroker() == broker || broker == null)
                .filter(carriage -> from == null || to == null || (carriage.getDate().plusDays(1).toLocalDate().isAfter(from)
                        && carriage.getDate().minusDays(1).toLocalDate().isBefore(to)))
                .forEach(optional::add);
        return optional;
    }

    public int getSize() {
        return optional.size();
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
