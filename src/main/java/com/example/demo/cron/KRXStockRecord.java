package com.example.demo.cron;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
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
}
