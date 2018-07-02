package com.yorix.registration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Builder {
    CarriagesList list;

    public Builder(CarriagesList list) {
        this.list = list;
        fillList();
    }

    private void fillList() {
        list.add(new Carriage(
                LocalDateTime.parse("01.01.2000 00:00", DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")),
                "",
                "",
                "",
                Broker.EXIM,
                "",
                ""
        ));
    }
}
