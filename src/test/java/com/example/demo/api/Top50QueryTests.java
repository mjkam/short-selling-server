package com.example.demo.api;

import com.example.demo.api.builder.FetchRecordBuilder;
import com.example.demo.api.builder.StockRecordBuilder;
import com.example.demo.domain.FetchRecord;
import com.example.demo.domain.StockRecord;
import com.example.demo.repository.FetchRecordRepository;
import com.example.demo.repository.StockRecordRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.example.demo.TimeUtils.localDate;
import static com.example.demo.api.builder.FetchRecordBuilder.*;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class Top50QueryTests {
    @Autowired
    private FetchRecordRepository fetchRecordRepository;
    @Autowired
    private StockRecordRepository stockRecordRepository;


    @Test
    void getStockRecordsQueryTest() {
        //given
        stockRecordRepository.save(StockRecordBuilder.stockRecord()
                .recordDate(localDate("2022-10-10"))
                .shortSellingRatio(1.0f)
                .build());
        stockRecordRepository.save(StockRecordBuilder.stockRecord()
                .recordDate(localDate("2022-10-11"))
                .shortSellingRatio(2.0f)
                .build());
        stockRecordRepository.save(StockRecordBuilder.stockRecord()
                .recordDate(localDate("2022-10-11"))
                .shortSellingRatio(3.0f)
                .build());
        stockRecordRepository.save(StockRecordBuilder.stockRecord()
                .recordDate(localDate("2022-10-11"))
                .shortSellingRatio(4.0f)
                .build());

        //when
        List<StockRecord> stockRecords = stockRecordRepository
                .findByRecordDateOrderByShortSellingRatioDesc(localDate("2022-10-11"), PageRequest.of(0, 3));

        //then
        assertThat(stockRecords.size()).isEqualTo(3);
        assertThatStockRecordsDate(stockRecords, localDate("2022-10-11"));
        assertThatStockRecordsDesc(stockRecords);

    }

    private void assertThatStockRecordsDesc(List<StockRecord> stockRecords) {
        for (int i = 1; i < stockRecords.size(); i++) {
            assertThat(stockRecords.get(i - 1).getShortSellingRatio()).isGreaterThanOrEqualTo(stockRecords.get(i).getShortSellingRatio());
        }
    }

    private void assertThatStockRecordsDate(List<StockRecord> stockRecords, LocalDate localDate) {
        stockRecords.forEach(o -> assertThat(o.getRecordDate()).isEqualTo(localDate));
    }

    @Test
    void getLatestFetchRecordTest() {
        //given
        FetchRecord record1 = fetchRecord().stockRecordDate(localDate("2022-10-01")).build();
        FetchRecord record2 = fetchRecord().stockRecordDate(localDate("2022-10-02")).build();
        FetchRecord expect = fetchRecord().stockRecordDate(localDate("2022-10-03")).build();
        fetchRecordRepository.save(record1);
        fetchRecordRepository.save(record2);
        fetchRecordRepository.save(expect);

        //when
        List<FetchRecord> latestFetchRecord = fetchRecordRepository.findByOrderByStockRecordDateDesc(PageRequest.of(0, 1));

        //then
        assertThat(latestFetchRecord.size()).isEqualTo(1);
        assertThat(expect.getStockRecordDate()).isEqualTo(latestFetchRecord.get(0).getStockRecordDate());
    }
}
