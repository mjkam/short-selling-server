package com.example.demo;

import java.time.LocalDate;

public class MockTimeManager extends TimeManager{
    private LocalDate fixedDate;

    public MockTimeManager(LocalDate localDate) {
        this.fixedDate = localDate;
    }

    @Override
    public LocalDate getCurrentDate() {
        return this.fixedDate;
    }
}