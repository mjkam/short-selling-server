package com.example.demo.cron;

import com.example.demo.domain.*;
import com.example.demo.repository.CompanyRepository;
import com.example.demo.repository.FetchRecordRepository;
import com.example.demo.repository.StockRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class StockRecordsSaver {
    private final CompanyRepository companyRepository;
    private final StockRecordRepository stockRecordRepository;
    private final FetchRecordRepository fetchRecordRepository;

    private final KRXApi krxApi;

    private void saveRecords(MarketType marketType, List<Company> companies, LocalDate searchDate) {
        List<KRXStockRecord> krxStockRecords = krxApi.getStockRecordsAt(searchDate, marketType);
        List<String> companyCodes = companies.stream()
                .map(Company::getCompanyCode)
                .collect(Collectors.toList());
        List<Company> newCompanies = companyRepository.saveAll(krxStockRecords.stream()
                .filter(krxStockRecord -> !companyCodes.contains(krxStockRecord.getCompanyCode()))
                .map(krxStockRecord -> new Company(krxStockRecord, marketType))
                .collect(Collectors.toList()));
        companies.addAll(newCompanies);

        List<StockRecord> result = new ArrayList<>();
        for (KRXStockRecord krxStockRecord: krxStockRecords) {
            Company company = companies.stream()
                    .filter(o -> o.getCompanyCode().equals(krxStockRecord.getCompanyCode()))
                    .findAny()
                    .orElseThrow(() -> new RuntimeException(""));
            result.add(new StockRecord(company, krxStockRecord, searchDate));
        }
        stockRecordRepository.saveAll(result);
    }

    @Transactional
    public void save(LocalDate searchDate) {
        List<Company> companies = companyRepository.findAll();
        saveRecords(MarketType.KOSPI, companies, searchDate);
        saveRecords(MarketType.KOSDAQ, companies, searchDate);
        fetchRecordRepository.save(new FetchRecord(searchDate, LocalDateTime.now()));
    }
}