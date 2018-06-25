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
                LocalDateTime.parse("21.06.2018 13:31", DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")),
                "",
                "",
                "",
                Broker.POLITRANS,
                "",
                ""
        ));

        list.add(new Carriage(
                LocalDateTime.parse("21.06.2018 17:44", DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")),
                "",
                "",
                "",
                Broker.EXIM,
                "",
                ""
        ));

    }
}
