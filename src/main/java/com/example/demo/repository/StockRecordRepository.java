package com.example.demo.repository;

import com.example.demo.domain.StockRecord;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface StockRecordRepository {
    List<StockRecord> findTop50(LocalDate stockRecordDate, Pageable pageable);
}
