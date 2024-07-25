package com.example.fpt_midterm_pos.utils;

import java.util.Date;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateUtils {
    private static final DateTimeFormatter DATE_FORMATER = DateTimeFormatter.ofPattern("d/M/yyyy");

    public static LocalDate parseDate(String dateStr) {
        try {
            return LocalDate.parse(dateStr, DATE_FORMATER);
        } catch (DateTimeParseException e) {
            System.out.println("Error parsing date: " + dateStr);
            throw e;
        }
    }
    public static String formatDate(LocalDate date) {
        return date.format(DATE_FORMATER);
    }
    public static LocalDate formatDateToLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

}
