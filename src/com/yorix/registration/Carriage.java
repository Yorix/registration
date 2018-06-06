package com.yorix.registration;

import javafx.beans.property.SimpleStringProperty;

import java.io.*;
import java.util.GregorianCalendar;

public class Carriage implements Externalizable {

    private FormattedDate date;
    private SimpleStringProperty consignee;
    private Broker broker;

    public Carriage() {
    }

    public Carriage(FormattedDate date, String consignee, Broker broker) {
        this.date = date;
        this.broker = broker;
        this.consignee = new SimpleStringProperty(consignee);
    }

    public FormattedDate getDate() {
        return date;
    }

    public void setDate(FormattedDate date) {
        this.date = date;
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
        out.writeObject(date.getDate());
        out.writeObject(consignee.getValue());
        out.writeObject(broker);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        date = new FormattedDate((GregorianCalendar) in.readObject());
        consignee = new SimpleStringProperty(in.readObject().toString());
        broker = (Broker) in.readObject();
    }
}
