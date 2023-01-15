package com.example.demo.controller.dto;

import com.example.demo.domain.StockRecord;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
public class GetStockRecordsResponse {
    private List<StockRecordDto> stockRecords;

    public GetStockRecordsResponse(List<StockRecord> stockRecords) {
        this.stockRecords = stockRecords.stream()
                .map(StockRecordDto::new)
                .collect(Collectors.toList());
    }
}
