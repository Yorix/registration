package com.yorix.registration;

import java.io.Serializable;

public enum Broker implements Serializable{
    POLITRANS, EXIM, PE;

    private String s = this.name().substring(0, 1);

    @Override
    public String toString() {
        return s;
    }
}
