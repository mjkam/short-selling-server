package com.example.demo.api.builder;

import com.example.demo.cron.KRXStockRecord;
import org.springframework.test.util.ReflectionTestUtils;

public final class KRXStockRecordBuilder {
    private String companyCode;
    private String companyName;
    private String shortSellingShareCount;
    private String listedShareCount;
    private String shortSellingAmount;
    private String listedShareAmount;
    private String shortSellingShareRatio;

    private KRXStockRecordBuilder() {
    }

    public static KRXStockRecordBuilder krxStockRecord() {
        return new KRXStockRecordBuilder();
    }

    public KRXStockRecordBuilder companyCode(String companyCode) {
        this.companyCode = companyCode;
        return this;
    }

    public KRXStockRecordBuilder companyName(String companyName) {
        this.companyName = companyName;
        return this;
    }

    public KRXStockRecordBuilder shortSellingShareCount(String shortSellingShareCount) {
        this.shortSellingShareCount = shortSellingShareCount;
        return this;
    }

    public KRXStockRecordBuilder listedShareCount(String listedShareCount) {
        this.listedShareCount = listedShareCount;
        return this;
    }

    public KRXStockRecordBuilder shortSellingAmount(String shortSellingAmount) {
        this.shortSellingAmount = shortSellingAmount;
        return this;
    }

    public KRXStockRecordBuilder listedShareAmount(String listedShareAmount) {
        this.listedShareAmount = listedShareAmount;
        return this;
    }

    public KRXStockRecordBuilder shortSellingShareRatio(String shortSellingShareRatio) {
        this.shortSellingShareRatio = shortSellingShareRatio;
        return this;
    }

    public KRXStockRecordBuilder but() {
        return krxStockRecord().companyCode(companyCode).companyName(companyName).shortSellingShareCount(shortSellingShareCount).listedShareCount(listedShareCount).shortSellingAmount(shortSellingAmount).listedShareAmount(listedShareAmount).shortSellingShareRatio(shortSellingShareRatio);
    }

    public KRXStockRecord build() {
        KRXStockRecord krxStockRecord = new KRXStockRecord();
        ReflectionTestUtils.setField(krxStockRecord, "companyCode", this.companyCode);
        ReflectionTestUtils.setField(krxStockRecord, "companyName", this.companyName);
        ReflectionTestUtils.setField(krxStockRecord, "shortSellingShareCount", this.shortSellingShareCount);
        ReflectionTestUtils.setField(krxStockRecord, "listedShareCount", this.listedShareCount);
        ReflectionTestUtils.setField(krxStockRecord, "shortSellingAmount", this.shortSellingAmount);
        ReflectionTestUtils.setField(krxStockRecord, "listedShareAmount", this.listedShareAmount);
        ReflectionTestUtils.setField(krxStockRecord, "shortSellingShareRatio", this.shortSellingShareRatio);

        return krxStockRecord;
    }
}
