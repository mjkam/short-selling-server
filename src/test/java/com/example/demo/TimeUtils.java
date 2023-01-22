package com.example.demo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeUtils {
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static LocalDate localDate(String s) {
        return LocalDate.parse(s, dateFormatter);
    }

    public static LocalDateTime localDateTime(String s) {
        return LocalDateTime.parse(s, dateTimeFormatter);
    }
}