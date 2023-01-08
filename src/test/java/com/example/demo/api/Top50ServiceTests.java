package com.example.demo.api;

import com.example.demo.domain.FetchRecord;
import com.example.demo.domain.StockRecord;
import com.example.demo.repository.FetchRecordRepository;
import com.example.demo.repository.StockRecordRepository;
import com.example.demo.service.Top50Service;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class Top50ServiceTests {
    private Top50Service top50Service = new Top50Service(
            new FetchRecordRepositoryMemoryImpl(), new StockRecordRepositoryMemoryImpl());

    @Test
    void getTop50Test() {
        //given
        int top50RecordNum = 50;

        //when
        List<StockRecord> top50Records = top50Service.getTop50();

        //then
        assertThat(top50Records.size()).isEqualTo(top50RecordNum);
    }

    private static class FetchRecordRepositoryMemoryImpl implements FetchRecordRepository {
        @Override
        public List<FetchRecord> findLatestOne(Pageable pageable) {
            if (pageable.getPageNumber() == 0 && pageable.getPageSize() == 1) {
                return List.of(new FetchRecord());
            }
            return new ArrayList<>();
        }
    }

    private static class StockRecordRepositoryMemoryImpl implements StockRecordRepository{
        @Override
        public List<StockRecord> findTop50(LocalDate stockRecordDate, Pageable pageable) {
            if (pageable.getPageSize() != 50 || pageable.getPageNumber() != 0) {
                throw new RuntimeException(String.format("expected: %d-%d, actual %d-%d", 0, 50, pageable.getPageNumber(), pageable.getPageSize()));
            }
            List<StockRecord> stockRecords = new ArrayList<>();
            for (int i=0; i<50; i++) {
                stockRecords.add(new StockRecord());
            }
            return stockRecords;
        }
    }

}
