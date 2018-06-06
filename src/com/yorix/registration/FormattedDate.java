package com.yorix.registration;

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class FormattedDate implements Serializable {
    private GregorianCalendar date;

    public FormattedDate() {
        date = new GregorianCalendar();
    }

    public FormattedDate(GregorianCalendar date) {
        this.date = date;
    }

    public GregorianCalendar getDate() {
        return date;
    }

    @Override
    public String toString() {
        return date.get(Calendar.DATE) + "." +
                (date.get(Calendar.MONTH) + 1) + "." +
                date.get(Calendar.YEAR) + " " +
                date.get(Calendar.HOUR_OF_DAY) + ":" +
                date.get(Calendar.MINUTE) + ":" +
                date.get(Calendar.SECOND);
    }
}
