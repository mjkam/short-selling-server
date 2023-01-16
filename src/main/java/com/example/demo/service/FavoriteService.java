package com.example.demo.service;

import com.example.demo.domain.FavoriteRecord;
import com.example.demo.repository.FavoriteRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
public class FavoriteService {
    private final FavoriteRecordRepository favoriteRecordRepository;

    public void registerFavorite(String companyCode) {
        FavoriteRecord favoriteRecord = favoriteRecordRepository.findByCompanyCode(companyCode)
                .orElse(null);
        if (favoriteRecord == null) {
            favoriteRecord = favoriteRecordRepository.save(new FavoriteRecord(companyCode));
        }
        favoriteRecord.incrementCount();
    }
}
