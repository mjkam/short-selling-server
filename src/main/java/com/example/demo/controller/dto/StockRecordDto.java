package com.example.demo.controller.dto;

import com.example.demo.domain.StockRecord;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class StockRecordDto {
    private String recordDate;
    private Long shortSellingShareCount;
    private Long listedShareCount;
    private Long shortSellingAmount;
    private Long listedShareAmount;
    private Float shortSellingRatio;

    public StockRecordDto(StockRecord stockRecord) {
        this.recordDate = stockRecord.getRecordDate().toString();
        this.shortSellingShareCount = stockRecord.getShortSellingShareCount();
        this.listedShareCount = stockRecord.getListedShareCount();
        this.shortSellingAmount = stockRecord.getShortSellingAmount();
        this.listedShareAmount = stockRecord.getListedShareAmount();
        this.shortSellingRatio = stockRecord.getShortSellingRatio();
    }
}
