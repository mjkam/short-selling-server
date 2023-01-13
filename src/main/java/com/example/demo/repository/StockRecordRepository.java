package com.example.demo.repository;

import com.example.demo.domain.StockRecord;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface StockRecordRepository extends JpaRepository<StockRecord, Long> {
    @Query("SELECT m FROM StockRecord m JOIN FETCH m.company WHERE m.recordDate = :recordDate ORDER BY m.shortSellingRatio DESC")
    List<StockRecord> findByRecordDateOrderByShortSellingRatioDesc(@Param("recordDate") LocalDate recordDate, Pageable pageable);
}