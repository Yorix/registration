package com.yorix.registration;

import javafx.beans.property.SimpleObjectProperty;
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
    private SimpleStringProperty carNumber;
    private SimpleStringProperty phoneNumber;
    private SimpleStringProperty consignee;
    private SimpleStringProperty broker;
    private SimpleStringProperty declarationId;

    public Carriage() {
    }

    public Carriage(LocalDateTime date, String carNumber, String phoneNumber, String consignee, Broker broker, String declarationId) {
        this.date = new SimpleStringProperty(date.format(DateTimeFormatter.ofPattern("dd.MM.YYYY HH.mm")));
        this.carNumber = new SimpleStringProperty(carNumber);
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
        this.consignee = new SimpleStringProperty(consignee);
        this.broker =  new SimpleStringProperty(broker.toString());
        this.declarationId = new SimpleStringProperty(declarationId);
    }

    public LocalDateTime getDate() {
        return LocalDateTime.parse(date.get(), DateTimeFormatter.ofPattern("dd.MM.YYYY HH.mm"));
    }

    public SimpleStringProperty dateProperty() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date.set(date.format(DateTimeFormatter.ofPattern("dd.MM.YYYY HH.mm")));
    }

    public String getCarNumber() {
        return carNumber.get();
    }

    public SimpleStringProperty carNumberProperty() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber.set(carNumber);
    }

    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    public SimpleStringProperty phoneNumberProperty() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber.set(phoneNumber);
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

    public Broker getBroker() {
        return Broker.valueOf(broker.get());
    }

    public SimpleStringProperty brokerProperty() {
        return broker;
    }

    public void setBroker(Broker broker) {
        this.broker.set(broker.toString());
    }

    public String getDeclarationId() {
        return declarationId.get();
    }

    public SimpleStringProperty declarationIdProperty() {
        return declarationId;
    }

    public void setDeclarationId(String declarationId) {
        this.declarationId.set(declarationId);
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
        out.writeObject(carNumber.get());
        out.writeObject(phoneNumber.get());
        out.writeObject(consignee.get());
        out.writeObject(broker.get());
        out.writeObject(declarationId.get());
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        date = new SimpleStringProperty(in.readObject().toString());
        carNumber = new SimpleStringProperty(in.readObject().toString());
        phoneNumber = new SimpleStringProperty(in.readObject().toString());
        consignee = new SimpleStringProperty(in.readObject().toString());
        broker = new SimpleStringProperty(in.readObject().toString());
        declarationId = new SimpleStringProperty(in.readObject().toString());
    }
}
