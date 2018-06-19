package com.yorix.registration;

import com.yorix.registration.io.InOut;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CarriagesList implements Externalizable {
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

//        carriages.add(new Carriage(
//                LocalDateTime.parse("20.11.2017 12:35", DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")),
//                "GG5566HH",
//                "0",
//                "srf",
//                Broker.POLITRANS,
//                "000"
//        ));
//        carriages.add(new Carriage(
//                LocalDateTime.parse("25.05.2018 12:35", DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")),
//                "GG5566HH",
//                "0",
//                "srf",
//                Broker.EXIM,
//                "000"
//        ));
//        carriages.add(new Carriage(
//                LocalDateTime.parse("01.06.2018 12:35", DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")),
//                "GG5566HH",
//                "0",
//                "srf",
//                Broker.POLITRANS,
//                "000"
//        ));
//        carriages.add(new Carriage(
//                LocalDateTime.parse("10.06.2018 12:35", DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")),
//                "GG5566HH",
//                "0",
//                "srf",
//                Broker.EXIM,
//                "000"
//        ));
    }

    public void delete(int index) {
        carriages.remove(index);
        InOut.write(this);
    }

    public void setCurrentBrocker(Broker currentBrocker) {
        this.currentBrocker = currentBrocker;
    }

    public void setCarriages(ObservableList<Carriage> carriages) {
        this.carriages = carriages;
    }

    public ObservableList<Carriage> getCarriages(LocalDate from, LocalDate to, Broker broker) {

//        if ((from == null || to == null) && broker == null) return carriages;
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
