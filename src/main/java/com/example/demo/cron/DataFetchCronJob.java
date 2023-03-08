package com.example.demo.cron;

import com.example.demo.TimeManager;
import com.example.demo.config.DataFetchProperties;
import com.example.demo.domain.FetchRecord;
import com.example.demo.repository.FetchRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DataFetchCronJob {
    private final StockRecordsApplier stockRecordsApplier;
    private final FetchRecordRepository fetchRecordRepository;
    private final TimeManager timeManager;
    private final DataFetchProperties dataFetchProperties;

    @Scheduled(cron = "0 1 * * * *")
    public void fetch() {
        LocalDate searchingDate = findLastFetchedDate();
        LocalDate searchEndDate = timeManager.getCurrentDate();

        while (searchingDate.isBefore(searchEndDate) || searchingDate.isEqual(searchEndDate)) {
            stockRecordsApplier.apply(searchingDate);
            searchingDate = searchingDate.plusDays(1L);
        }
    }

    private LocalDate findLastFetchedDate() {
        FetchRecord fetchRecord = fetchRecordRepository.findByOrderByStockRecordDateDesc(PageRequest.of(0, 1)).stream()
                .findAny()
                .orElse(null);
        return fetchRecord != null ?
                fetchRecord.getNextStockRecordDate() : dataFetchProperties.getFetchStartDate();
    }
}
