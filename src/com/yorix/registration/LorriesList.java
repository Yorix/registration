package com.yorix.registration;

import com.yorix.registration.io.Reader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class LorriesList {
    private ObservableList<Lorry> lorries;

    public LorriesList() {
        lorries = FXCollections.observableArrayList(Reader.read());
    }

    public void add(Lorry lorry) {
        lorries.add(lorry);
    }

    public ObservableList<Lorry> getLorries() {
        return lorries;
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
