package com.example.demo.api;

import com.example.demo.domain.FetchRecord;
import com.example.demo.repository.FetchRecordRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
public class Top50QueryTests {
    @Autowired
    private FetchRecordRepository fetchRecordRepository;

    private FetchRecord fetchRecord(LocalDate stockRecordDate, LocalDateTime executionDateTime) {
        FetchRecord fetchRecord = new FetchRecord();
        ReflectionTestUtils.setField(fetchRecord, "stockRecordDate", stockRecordDate);
        ReflectionTestUtils.setField(fetchRecord, "executedDateTime", executionDateTime);

        return fetchRecord;
    }

    private LocalDate localDate(String s) {
        return LocalDate.parse(s, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    private LocalDateTime localDateTime(String s) {
        return LocalDateTime.parse(s, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

//    @Test
//    void getTop50StockRecordsTest() {
//
//    }

    @Test
    void getLatestFetchRecordTest() {
        //given
        LocalDate stockRecordDate = localDate("2022-10-10");
        LocalDateTime executionDateTime = localDateTime("2022-12-12 00:00:00");
        FetchRecord fetchRecord = fetchRecordRepository.save(fetchRecord(stockRecordDate, executionDateTime));

        //when
        List<FetchRecord> latestFetchRecord = fetchRecordRepository.findByOrderByStockRecordDateDesc(PageRequest.of(0, 1));

        //then
        assertThat(latestFetchRecord.size()).isEqualTo(1);
        assertThat(fetchRecord.getStockRecordDate()).isEqualTo(latestFetchRecord.get(0).getStockRecordDate());
        assertThat(fetchRecord.getExecutedDateTime()).isEqualTo(latestFetchRecord.get(0).getExecutedDateTime());
    }
}
