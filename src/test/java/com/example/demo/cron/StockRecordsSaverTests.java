package com.example.demo.cron;

import com.example.demo.TimeUtils;
import com.example.demo.domain.FetchRecord;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class StockRecordsSaverTests {
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private StockRecordRepository stockRecordRepository;
    @Autowired
    private FetchRecordRepository fetchRecordRepository;
    @Autowired
    private KRXApi krxApi;

    @AfterEach
    @BeforeEach
    void init() {
        companyRepository.deleteAll();;
        stockRecordRepository.deleteAll();
        fetchRecordRepository.deleteAll();
    }


    @Test
    void saveStockRecords() throws JsonProcessingException {
        //given
        StockRecordsSaver stockRecordsSaver = new StockRecordsSaver(
                companyRepository, stockRecordRepository, fetchRecordRepository, krxApi
        );

        //when
        stockRecordsSaver.save(TimeUtils.localDate("2022-10-12"));

        //then
        List<FetchRecord> fetchRecords = fetchRecordRepository.findAll();
        assertThat(fetchRecords.size()).isEqualTo(1);
        assertThat(fetchRecords.get(0).getStockRecordDate()).isEqualTo(TimeUtils.localDate("2022-10-12"));

        assertThat(companyRepository.findAll().size()).isEqualTo(stockRecordRepository.findAll().size());
    }
}
