package com.example.demo.cron;

import com.example.demo.Constants;
import com.example.demo.TimeManager;
import com.example.demo.domain.FetchRecord;
import com.example.demo.repository.FetchRecordRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.Schedules;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DataFetchCronJob {
    private final StockRecordsSaver stockRecordsSaver;
    private final FetchRecordRepository fetchRecordRepository;
    private final TimeManager timeManager;

    @Schedules({
            @Scheduled(cron = "0 1 9 * * *"),
            @Scheduled(cron = "0 0 * * * *"),
    })
    public void fetch() throws JsonProcessingException {
        System.setProperty("https.protocols", "TLSv1.2,TLSv1.1,TLSv1");
        FetchRecord fetchRecord = fetchRecordRepository.findByOrderByStockRecordDateDesc(PageRequest.of(0, 1)).stream()
                .findAny()
                .orElse(null);
        LocalDate searchDate = fetchRecord != null ? fetchRecord.getNextStockRecordDate() : Constants.stockRecordStartDate;
        LocalDate searchEndDate = timeManager.getCurrentDate();

        while (searchDate.isBefore(searchEndDate) || searchDate.isEqual(searchEndDate)) {
            stockRecordsSaver.save(searchDate);
            searchDate = searchDate.plusDays(1L);
        }
    }
}
