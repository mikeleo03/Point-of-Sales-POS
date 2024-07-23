package com.example.fpt_midterm_pos.utils;

import java.time.LocalDate;
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

}
