package com.example.demo.service;

import com.example.demo.domain.FetchRecord;
import com.example.demo.domain.StockRecord;
import com.example.demo.repository.FetchRecordRepository;
import com.example.demo.repository.StockRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class Top50Service {
    private final FetchRecordRepository fetchRecordRepository;
    private final StockRecordRepository stockRecordRepository;

    @Transactional(readOnly = true)
    public List<StockRecord> getTop50() {
        FetchRecord lastestFetchRecord = findLatestFetchRecord();
        return findTop50StockRecords(lastestFetchRecord.getStockRecordDate());
    }

    private FetchRecord findLatestFetchRecord() {
        return fetchRecordRepository.findByOrderByStockRecordDateDesc(PageRequest.of(0, 1)).stream()
                .findAny()
                .orElseThrow(() -> new IllegalStateException("FetchRecord not found"));
    }

    private List<StockRecord> findTop50StockRecords(LocalDate localDate) {
        List<StockRecord> top50StockRecords = stockRecordRepository
                .findByRecordDateOrderByShortSellingRatioDesc(localDate, PageRequest.of(0, 50));
        if (top50StockRecords.size() != 50) {
            throw new IllegalStateException(String.format("top50 size expected 50, actual: %d", top50StockRecords.size()));
        }
        return top50StockRecords;
    }
}
