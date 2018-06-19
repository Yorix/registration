package com.yorix.registration;

import java.io.Serializable;

public enum Broker implements Serializable {
    POLITRANS, EXIM;

    private String s = this.name();

    @Override
    public String toString() {
        return s;
    }
}
