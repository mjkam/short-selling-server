package com.example.demo.service;

import com.example.demo.domain.FavoriteRecord;
import com.example.demo.exception.ClientBadRequestException;
import com.example.demo.repository.CompanyRepository;
import com.example.demo.repository.FavoriteRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class FavoriteService {
    private final CompanyRepository companyRepository;
    private final FavoriteRecordRepository favoriteRecordRepository;

    public void registerFavorite(String companyCode) {
        if (companyRepository.findByCompanyCode(companyCode).isEmpty()) {
            throw new ClientBadRequestException(String.format("%s is not registered companyCode", companyCode));
        }

        FavoriteRecord favoriteRecord = favoriteRecordRepository.findByCompanyCode(companyCode)
                .orElseGet(() -> favoriteRecordRepository.save(new FavoriteRecord(companyCode)));
        favoriteRecord.incrementCount();
    }
}
