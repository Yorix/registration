package com.yorix.registration;

import javafx.beans.property.SimpleStringProperty;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalField;

public class Carriage implements Externalizable {

    private SimpleStringProperty date;
    private Car car;
    private SimpleStringProperty consignee;
    private Broker broker;

    public Carriage() {
    }

    public Carriage(LocalDateTime date, Car car, String consignee, Broker broker) {
        this.date = new SimpleStringProperty(date.format(DateTimeFormatter.ofPattern("DD.MM.YYYY hh.mm")));
        this.car = car;
        this.consignee = new SimpleStringProperty(consignee);
        this.broker = broker;
    }

    public LocalDate getDate() {
        return LocalDate.parse(date.get());
    }

    public SimpleStringProperty dateProperty() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date.set(date.format(DateTimeFormatter.ofPattern("DD.MM.YYYY hh.mm")));
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public String getConsignee() {
        return consignee.get();
    }

    public SimpleStringProperty consigneeProperty() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee.set(consignee);
    }

    public void setBroker(Broker broker) {
        this.broker = broker;
    }

    public Broker getBroker() {
        return broker;
    }

    @Override
    public String toString() {
        return "Carriage{" +
                "date=" + date +
                ", consignee='" + consignee + '\'' +
                ", broker=" + broker +
                '}';
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(date.get());
        out.writeObject(car);
        out.writeObject(consignee.get());
        out.writeObject(broker);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        date = new SimpleStringProperty(in.readObject().toString());
        car = (Car) in.readObject();
        consignee = new SimpleStringProperty(in.readObject().toString());
        broker = (Broker) in.readObject();
    }
}
