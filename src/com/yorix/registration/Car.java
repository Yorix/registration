package com.yorix.registration;

import javafx.beans.property.SimpleStringProperty;

import java.io.*;

public class Car implements Externalizable {
    private SimpleStringProperty carNumber;
    private SimpleStringProperty phoneNumber;

    public Car() {
    }

    public Car(String carNumber, String phoneNumber) {
        this.carNumber = new SimpleStringProperty(carNumber);
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
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

    @Override
    public String toString() {
        return "Car{" +
                "carNumber=" + carNumber +
                ", phoneNumber=" + phoneNumber +
                '}';
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(carNumber.getValue());
        out.writeObject(phoneNumber.getValue());
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        carNumber = new SimpleStringProperty(in.readObject().toString());
        phoneNumber = new SimpleStringProperty(in.readObject().toString());
    }
}
