package com.example.demo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TimeUtils {
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static LocalDate localDate(String s) {
        return LocalDate.parse(s, dateFormatter);
    }
}
