package com.yorix.registration;

import javafx.beans.property.SimpleStringProperty;

import java.io.Serializable;
import java.util.*;

public class Lorry implements Serializable {
    private SimpleStringProperty idNumber;
    private SimpleStringProperty phoneNumber;
    private Broker broker;
    private List<Carriage> carriages;

    public Lorry(String idNumber, String phoneNumber, String consignee, Broker broker) {
        this.idNumber = new SimpleStringProperty(idNumber);
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
        carriages = new LinkedList<>();
        carriages.add(new Carriage(new FormattedDate(), consignee, broker));
        this.broker = broker;
    }

    public List<Carriage> getCarriages() {
        return carriages;
    }

    public void setBroker() {
        int p = 0;
        int e = 0;
        for (Carriage carriage : carriages) {
            if (carriage.getBroker() == Broker.POLITRANS) p++;
            else e++;
        }
        if (p == e) broker = Broker.PE;
        else broker = p > e ? Broker.POLITRANS : Broker.EXIM;
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
}
