package com.example.demo.api.builder;

import com.example.demo.domain.StockRecord;

import java.time.LocalDate;

public class StockRecordBuilder {
    private Long companyId = 1L;
    private LocalDate recordDate;
    private Long shortSellingShareCount = 0L;
    private Long listedShareCount = 0L;
    private Long shortSellingAmount = 0L;
    private Long listedShareAmount = 0L;
    private Float shortSellingRatio;

    private StockRecordBuilder() {}

    public static StockRecordBuilder stockRecord() {
        return new StockRecordBuilder();
    }

    public StockRecordBuilder recordDate(LocalDate localDate) {
        this.recordDate = localDate;
        return this;
    }

    public StockRecordBuilder shortSellingRatio(float num) {
        this.shortSellingRatio = num;
        return this;
    }

    public StockRecord build() {
        return new StockRecord(
                this.companyId,
                this.recordDate,
                this.shortSellingShareCount,
                this.listedShareCount,
                this.shortSellingAmount,
                this.listedShareAmount,
                this.shortSellingRatio
        );
    }
}