package com.example.demo.repository;

import com.example.demo.domain.FavoriteRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FavoriteRecordRepository extends JpaRepository<FavoriteRecord, Long> {
    Optional<FavoriteRecord> findByCompanyCode(String companyCode);
}
