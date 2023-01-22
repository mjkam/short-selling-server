package com.example.demo.cron;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

@NoArgsConstructor
public class KRXStockRecord {
    @JsonProperty("ISU_CD")
    private String companyCode;
    @JsonProperty("ISU_ABBRV")
    private String companyName;
    @JsonProperty("BAL_QTY")
    private String shortSellingShareCount;
    @JsonProperty("LIST_SHRS")
    private String listedShareCount;
    @JsonProperty("BAL_AMT")
    private String shortSellingAmount;
    @JsonProperty("MKTCAP")
    private String listedShareAmount;
    @JsonProperty("BAL_RTO")
    private String shortSellingShareRatio;

    public String getCompanyCode() {
        return this.companyCode;
    }

    public String getCompanyName() {
        return this.companyName;
    }

    public long getShortSellingShareCount() {
        return changeStringToLong(this.shortSellingShareCount);
    }

    public long getListedShareCount() {
        return changeStringToLong(this.listedShareCount);
    }

    public long getShortSellingAmount() {
        return changeStringToLong(this.shortSellingAmount);
    }

    public long getListedShareAmount() {
        return changeStringToLong(this.listedShareAmount);
    }

    public float getShortSellingShareRatio() {
        return Float.parseFloat(this.shortSellingShareRatio);
    }

    private long changeStringToLong(String s) {
        try {
            return NumberFormat.getNumberInstance(Locale.US).parse(s).longValue();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}