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
        try {
            return NumberFormat.getNumberInstance(Locale.US).parse(this.shortSellingShareCount).longValue();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public long getListedShareCount() {
        try {
            return NumberFormat.getNumberInstance(Locale.US).parse(this.listedShareCount).longValue();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public long getShortSellingAmount() {
        try {
            return NumberFormat.getNumberInstance(Locale.US).parse(this.shortSellingAmount).longValue();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public long getListedShareAmount() {
        try {
            return NumberFormat.getNumberInstance(Locale.US).parse(this.listedShareAmount).longValue();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public float getShortSellingShareRatio() {
        return Float.parseFloat(this.shortSellingShareRatio);
    }
}