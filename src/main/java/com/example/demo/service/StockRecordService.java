package com.example.demo.service;

import com.example.demo.TimeManager;
import com.example.demo.domain.StockRecord;
import com.example.demo.repository.StockRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StockRecordService {
    private final StockRecordRepository stockRecordRepository;
    private final TimeManager timeManager;

    public List<StockRecord> getStockRecords(String companyCode, int duration) {
        return stockRecordRepository
                .findByRecordDateOrderByRecordDateASC(companyCode, timeManager.getCurrentDate().minusDays(duration));
    }
}
