package com.example.demo;

import com.example.demo.cron.StockRecordsSaver;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MockStockRecordSaver extends StockRecordsSaver {
    private List<LocalDate> requestedDates = new ArrayList<>();

    public MockStockRecordSaver() {
        super(null, null, null, null);
    }

    @Override
    public void save(LocalDate localDate) {
        this.requestedDates.add(localDate);
    }

    public List<LocalDate> getRequestDates() {
        return this.requestedDates;
    }
}
