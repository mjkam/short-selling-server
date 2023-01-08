package com.example.demo.repository;

import com.example.demo.domain.FetchRecord;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;

public interface FetchRecordRepository {
    List<FetchRecord> findLatestOne(Pageable pageable);
}
