package com.example.demo.controller;

import com.example.demo.controller.dto.GetStockRecordsResponse;
import com.example.demo.service.StockRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StockRecordController {
    private final StockRecordService stockRecordService;

    @GetMapping("/company/{companyCode}/stock-records")
    public GetStockRecordsResponse getStockRecords(
            @PathVariable("companyCode") String companyCode,
            @RequestParam("duration") int duration
    ) {
        return new GetStockRecordsResponse(stockRecordService.getStockRecords(companyCode, duration));
    }
}
