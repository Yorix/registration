package com.yorix.registration;

import javafx.beans.property.SimpleStringProperty;

import java.util.*;

public class Lorry {
    private SimpleStringProperty idNumber;
    private SimpleStringProperty phoneNumber;
    private List<FormattedDate> dates;
    private Broker broker;

    public Lorry(String idNumber, String phoneNumber, Broker broker) {
        this.idNumber = new SimpleStringProperty(idNumber);
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
        dates = new LinkedList<>();
        dates.add(new FormattedDate());
        this.broker = broker;
    }

    public void addCurrentDate() {
        dates.add(new FormattedDate());
    }

    public List<FormattedDate> getDates() {
        return dates;
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
}
