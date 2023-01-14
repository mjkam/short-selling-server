package com.example.demo.cron;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class GetKRXStockRecordsResponse {
    @JsonProperty("OutBlock_1")
    private List<KRXStockRecord> records;
    @JsonProperty("CURRENT_DATETIME")
    private String serverDateTimeStr;
}
