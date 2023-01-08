package com.example.demo.service;

import com.example.demo.domain.FetchRecord;
import com.example.demo.domain.StockRecord;
import com.example.demo.repository.FetchRecordRepository;
import com.example.demo.repository.StockRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class Top50Service {
    private final FetchRecordRepository fetchRecordRepository;
    private final StockRecordRepository stockRecordRepository;

    public List<StockRecord> getTop50() {
        FetchRecord lastestFetchRecord = fetchRecordRepository.findLatestOne(PageRequest.of(0, 1)).stream()
                .findAny()
                .orElse(null);
        // Todo: add when null
        return stockRecordRepository.findTop50(lastestFetchRecord.getStockRecordDate(), PageRequest.of(0, 50));
    }
}
