package com.example.demo.cron;

import com.example.demo.MockTimeManager;
import com.example.demo.TimeManager;
import com.example.demo.TimeUtils;
import com.example.demo.domain.FetchRecord;
import com.example.demo.domain.StockRecord;
import com.example.demo.repository.CompanyRepository;
import com.example.demo.repository.FetchRecordRepository;
import com.example.demo.repository.StockRecordRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static com.example.demo.TimeUtils.localDate;
import static com.example.demo.api.builder.FetchRecordBuilder.fetchRecord;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class DataFetchCronJobTests {
    @Autowired
    private FetchRecordRepository fetchRecordRepository;
    @Autowired
    private StockRecordRepository stockRecordRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private StockRecordsSaver stockRecordsSaver;


    private DataFetchCronJob dataFetchCronJob;

    @AfterEach
    @BeforeEach
    void setup() {
        System.setProperty("https.protocols", "TLSv1.2,TLSv1.1,TLSv1");
        fetchRecordRepository.deleteAll();
        stockRecordRepository.deleteAll();
        companyRepository.deleteAll();
    }

    @Test
    void fetchDataTest() throws JsonProcessingException {
        //given
        dataFetchCronJob = new DataFetchCronJob(
            stockRecordsSaver,
            fetchRecordRepository,
            new MockTimeManager(localDate("2022-10-13"))
        );
        FetchRecord fetchRecord = fetchRecord()
                .stockRecordDate(localDate("2022-10-12"))
                .build();
        fetchRecordRepository.save(fetchRecord);

        //when
        dataFetchCronJob.fetch();

        //then
        assertFetchRecordSavedAt(localDate("2022-10-13"));
        assertStockRecordsSavedAt(localDate("2022-10-13"));
    }

    private void assertFetchRecordSavedAt(LocalDate localDate) {
        List<FetchRecord> fetchRecords = fetchRecordRepository.findAll();
        boolean exist = fetchRecords.stream()
                .anyMatch(o -> o.getStockRecordDate().equals(localDate));
        assertThat(exist).isTrue();
    }

    private void assertStockRecordsSavedAt(LocalDate localDate) {
        List<StockRecord> all = stockRecordRepository.findAll();
        boolean b = all.stream()
                .anyMatch(o -> o.getRecordDate().equals(localDate));
        assertThat(b).isTrue();
    }
}
