package com.example.demo;

import com.example.demo.config.DataFetchProperties;

import java.time.LocalDate;

public class MockDataFetchProperties extends DataFetchProperties {
    private final LocalDate fixedDate;

    public MockDataFetchProperties(LocalDate localDate) {
        this.fixedDate = localDate;
    }

    @Override
    public LocalDate getFetchStartDate() {
        return this.fixedDate;
    }
}
