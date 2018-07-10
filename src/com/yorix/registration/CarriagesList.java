package com.yorix.registration;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;

public class CarriagesList {
    private ObservableList<Carriage> carriages;
    private ObservableList<Carriage> optional;
    private Broker currentBrocker;

    public CarriagesList() {
        carriages = FXCollections.observableArrayList();
        optional = FXCollections.observableArrayList();
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
}
