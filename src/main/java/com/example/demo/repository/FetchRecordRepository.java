package com.example.demo.repository;

import com.example.demo.domain.FetchRecord;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FetchRecordRepository extends JpaRepository<FetchRecord, Long> {
    @Query("SELECT m FROM FetchRecord m ORDER BY m.stockRecordDate DESC")
    List<FetchRecord> findByOrderByStockRecordDateDesc(Pageable pageable);
}
