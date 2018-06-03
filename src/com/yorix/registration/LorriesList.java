package com.yorix.registration;

import com.yorix.registration.io.InOut;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class LorriesList {
    private ObservableList<Lorry> lorries;

    public LorriesList() {
        lorries = FXCollections.observableArrayList(InOut.read());
    }

    public LorriesList(int empty) {
        lorries = FXCollections.observableArrayList();
    }

    public void add(Lorry lorry) {
        lorries.add(lorry);
        InOut.write(this);
    }

    public ObservableList<Lorry> getLorries() {
        return lorries;
    }

    public Lorry[] getLorriesAsArray() {


        return lorries.toArray(new Lorry[0]);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Lorry lorry : lorries) {
            builder.append(lorry).append("\n");
        }
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }
}
