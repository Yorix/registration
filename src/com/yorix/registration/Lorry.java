package com.yorix.registration;

import javafx.beans.property.SimpleStringProperty;

import java.io.*;

public class Lorry implements Externalizable {
    private SimpleStringProperty idNumber;
    private SimpleStringProperty phoneNumber;
    private Broker broker;
    private CarriagesList carriages;

    public Lorry() {
    }

    public Lorry(String idNumber, String phoneNumber, String consignee, Broker broker) {
        this.idNumber = new SimpleStringProperty(idNumber);
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
        carriages = new CarriagesList(this);
        carriages.add(this, new Carriage(new FormattedDate(), consignee, broker));
        this.broker = broker;
    }

    public String getIdNumber() {
        return idNumber.get();
    }

    public SimpleStringProperty idNumberProperty() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber.set(idNumber);
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

    public Broker getBroker() {
        return broker;
    }

    public void setBroker(Broker broker) {
        this.broker = broker;
    }

    public CarriagesList getCarriages() {
        return carriages;
    }

    public void setCarriages(CarriagesList carriages) {
        this.carriages = carriages;
    }

    public void setBroker() {
        int p = 0;
        int e = 0;
        for (Carriage carriage : carriages.getCarriages()) {
            if (carriage.getBroker() == Broker.POLITRANS) p++;
            else e++;
        }

        broker = p >= e ? Broker.POLITRANS : Broker.EXIM;
    }

    public String getBrokerVal() {
        return broker.toString();
    }

    @Override
    public String toString() {
        return "\nLorry{" +
                "idNumber=" + idNumber +
                ", phoneNumber=" + phoneNumber +
                ", broker=" + broker +
                ", carriages=" + carriages +
                "}";
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(idNumber.getValue());
        out.writeObject(phoneNumber.getValue());
        out.writeObject(broker);
        out.writeObject(carriages);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        idNumber = new SimpleStringProperty(in.readObject().toString());
        phoneNumber = new SimpleStringProperty(in.readObject().toString());
        broker = (Broker) in.readObject();
        carriages = (CarriagesList) in.readObject();
    }
}
