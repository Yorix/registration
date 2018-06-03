package com.yorix.registration;

import java.io.Serializable;

public class Carriage implements Serializable{

    private FormattedDate date;
    private Broker broker;
    private String consignee;

    public Carriage(FormattedDate date, String consignee, Broker broker) {
        this.date = date;
        this.broker = broker;
        this.consignee = consignee;
    }

    public Broker getBroker() {
        return broker;
    }

    @Override
    public String toString() {
        return "Carriage{" +
                "date=" + date +
                ", broker=" + broker +
                ", consignee=" + consignee +
                '}';
    }
}
