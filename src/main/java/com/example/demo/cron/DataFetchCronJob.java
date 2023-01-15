package com.example.demo.cron;

import com.example.demo.TimeManager;
import com.example.demo.domain.FetchRecord;
import com.example.demo.repository.FetchRecordRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DataFetchCronJob {
    private final StockRecordsSaver stockRecordsSaver;
    private final FetchRecordRepository fetchRecordRepository;
    private final TimeManager timeManager;

    public void fetch() throws JsonProcessingException {
        FetchRecord fetchRecord = fetchRecordRepository.findByOrderByStockRecordDateDesc(PageRequest.of(0, 1)).stream()
                .findAny()
                .orElseThrow(() -> new RuntimeException("Initial Data Not Exist"));
        LocalDate searchDate = fetchRecord.getNextStockRecordDate();
        LocalDate searchEndDate = timeManager.getCurrentDate();

        while (searchDate.isBefore(searchEndDate) || searchDate.isEqual(searchEndDate)) {
            stockRecordsSaver.save(searchDate);
            searchDate = searchDate.plusDays(1L);
        }
    }
}
