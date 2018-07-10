package com.yorix.registration;

public enum Broker {
    POLITRANS, EXIM;

    private String s = this.name();

    @Override
    public String toString() {
        return s;
    }
}
