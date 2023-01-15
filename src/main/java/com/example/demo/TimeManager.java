package com.example.demo;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class TimeManager {
    public LocalDate getCurrentDate() {
        return LocalDate.now();
    }
}
