package com.example.demo.api.builder;

import com.example.demo.domain.Company;
import com.example.demo.domain.StockRecord;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;

public final class StockRecordBuilder {
    private Long id;
    private Company company;
    private LocalDate recordDate;
    private Long shortSellingShareCount = 0L;
    private Long listedShareCount = 0L;
    private Long shortSellingAmount = 0L;
    private Long listedShareAmount = 0L;
    private Float shortSellingRatio = 0f;

    private StockRecordBuilder() {
    }

    public static StockRecordBuilder stockRecord() {
        return new StockRecordBuilder();
    }

    public StockRecordBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public StockRecordBuilder company(Company company) {
        this.company = company;
        return this;
    }

    public StockRecordBuilder recordDate(LocalDate recordDate) {
        this.recordDate = recordDate;
        return this;
    }

    public StockRecordBuilder shortSellingShareCount(Long shortSellingShareCount) {
        this.shortSellingShareCount = shortSellingShareCount;
        return this;
    }

    public StockRecordBuilder listedShareCount(Long listedShareCount) {
        this.listedShareCount = listedShareCount;
        return this;
    }

    public StockRecordBuilder shortSellingAmount(Long shortSellingAmount) {
        this.shortSellingAmount = shortSellingAmount;
        return this;
    }

    public StockRecordBuilder listedShareAmount(Long listedShareAmount) {
        this.listedShareAmount = listedShareAmount;
        return this;
    }

    public StockRecordBuilder shortSellingRatio(Float shortSellingRatio) {
        this.shortSellingRatio = shortSellingRatio;
        return this;
    }

    public StockRecordBuilder but() {
        return stockRecord().id(id).company(company).recordDate(recordDate).shortSellingShareCount(shortSellingShareCount).listedShareCount(listedShareCount).shortSellingAmount(shortSellingAmount).listedShareAmount(listedShareAmount).shortSellingRatio(shortSellingRatio);
    }

    public StockRecord build() {
        StockRecord stockRecord = new StockRecord();
        ReflectionTestUtils.setField(stockRecord, "id", this.id);
        ReflectionTestUtils.setField(stockRecord, "company", this.company);
        ReflectionTestUtils.setField(stockRecord, "recordDate", this.recordDate);
        ReflectionTestUtils.setField(stockRecord, "shortSellingShareCount", this.shortSellingShareCount);
        ReflectionTestUtils.setField(stockRecord, "listedShareCount", this.listedShareCount);
        ReflectionTestUtils.setField(stockRecord, "shortSellingAmount", this.shortSellingAmount);
        ReflectionTestUtils.setField(stockRecord, "listedShareAmount", this.listedShareAmount);
        ReflectionTestUtils.setField(stockRecord, "shortSellingRatio", this.shortSellingRatio);

        return stockRecord;
    }
}
