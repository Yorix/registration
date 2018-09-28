package com.yorix.registration;

import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Carriage {

    private SimpleStringProperty date;
    private SimpleStringProperty carNumber;
    private SimpleStringProperty phoneNumber;
    private SimpleStringProperty consignee;
    private SimpleStringProperty broker;
    private SimpleStringProperty declarationId;
    private SimpleStringProperty additionalInformation;

    public Carriage() {
        date = new SimpleStringProperty();
        carNumber = new SimpleStringProperty();
        phoneNumber = new SimpleStringProperty();
        consignee = new SimpleStringProperty();
        broker = new SimpleStringProperty();
        declarationId = new SimpleStringProperty();
        additionalInformation = new SimpleStringProperty();
    }

    public Carriage(LocalDateTime date,
                    String carNumber,
                    String phoneNumber,
                    String consignee,
                    Broker broker,
                    String declarationId,
                    String additionalInformation) {
        this.date = new SimpleStringProperty(date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
        this.carNumber = new SimpleStringProperty(carNumber);
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
        this.consignee = new SimpleStringProperty(consignee);
        this.broker = new SimpleStringProperty(broker.toString());
        this.declarationId = new SimpleStringProperty(declarationId);
        this.additionalInformation = new SimpleStringProperty(additionalInformation);
    }

    public LocalDateTime getDate() {
        return LocalDateTime.parse(date.get(), DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
    }

    public SimpleStringProperty dateProperty() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date.set(date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
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

    public String getAdditionalInformation() {
        return additionalInformation.get();
    }

    public SimpleStringProperty additionalInformationProperty() {
        return additionalInformation;
    }

    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation.set(additionalInformation);
    }

    @Override
    public String toString() {
        return "Carriage{" +
                "date=" + date +
                ", consignee='" + consignee + '\'' +
                ", broker=" + broker +
                '}';
    }
}
