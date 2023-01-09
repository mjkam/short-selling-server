package com.example.demo.repository;

import com.example.demo.domain.FetchRecord;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FetchRecordRepository extends JpaRepository<FetchRecord, Long> {
    List<FetchRecord> findLatestOne(Pageable pageable);
}
