package com.example.demo.service;

import com.example.demo.domain.FetchRecord;
import com.example.demo.domain.StockRecord;
import com.example.demo.repository.FetchRecordRepository;
import com.example.demo.repository.StockRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class Top50Service {
    private final FetchRecordRepository fetchRecordRepository;
    private final StockRecordRepository stockRecordRepository;

    @Transactional(readOnly = true)
    public List<StockRecord> getTop50() {
        FetchRecord lastestFetchRecord = fetchRecordRepository.findByOrderByStockRecordDateDesc(PageRequest.of(0, 1)).stream()
                .findAny()
                .orElseThrow(() -> new EntityNotFoundException("FetchRecord not found"));
        List<StockRecord> top50 = stockRecordRepository
                .findByRecordDateOrderByShortSellingRatioDesc(lastestFetchRecord.getStockRecordDate(), PageRequest.of(0, 50));
        if (top50.size() != 50) {
            throw new IllegalStateException(String.format("top50 size expected 50, actual: %d", top50.size()));
        }
        return top50;
    }
}
