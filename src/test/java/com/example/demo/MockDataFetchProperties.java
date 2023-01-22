package com.example.demo;

import com.example.demo.config.DataFetchProperties;

import java.time.LocalDate;

public class MockDataFetchProperties extends DataFetchProperties {
    private final LocalDate fixedDate;

    public MockDataFetchProperties(LocalDate localDate) {
        this.fixedDate = localDate;
    }

    public MockDataFetchProperties() {
        this.fixedDate = TimeUtils.localDate("2000-01-01");
    }

    @Override
    public LocalDate getFetchStartDate() {
        return this.fixedDate;
    }
}
