package com.example.demo;

import com.example.demo.cron.StockRecordsApplier;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MockStockRecordApplier extends StockRecordsApplier {
    private List<LocalDate> requestedDates = new ArrayList<>();

    public MockStockRecordApplier() {
        super(null, null, null, null);
    }

    @Override
    public void apply(LocalDate localDate) {
        this.requestedDates.add(localDate);
    }

    public List<LocalDate> getRequestDates() {
        return this.requestedDates;
    }
}
