package com.example.demo.cron;

import com.example.demo.domain.Company;
import com.example.demo.domain.FetchRecord;
import com.example.demo.domain.MarketType;
import com.example.demo.domain.StockRecord;
import com.example.demo.repository.CompanyRepository;
import com.example.demo.repository.FetchRecordRepository;
import com.example.demo.repository.StockRecordRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StockRecordsSaver {
    private final CompanyRepository companyRepository;
    private final StockRecordRepository stockRecordRepository;
    private final FetchRecordRepository fetchRecordRepository;

    private final KRXApi krxApi;


    @Transactional
    public void save(LocalDate searchDate) throws JsonProcessingException {
        List<KRXStockRecord> kospiRecords = krxApi.getStockRecordsAt(searchDate, MarketType.KOSPI);
        List<KRXStockRecord> kosdaqRecords = krxApi.getStockRecordsAt(searchDate, MarketType.KOSDAQ);
        List<KRXStockRecord> krxStockRecords = new ArrayList<>();
        krxStockRecords.addAll(kospiRecords);
        krxStockRecords.addAll(kosdaqRecords);
        if (krxStockRecords.size() == 0) {
            return;
        }
        List<Company> companies = companyRepository.findAll();
        List<Company> newCompanies = new ArrayList<>();
        List<StockRecord> newStockRecords = new ArrayList<>();
        for (KRXStockRecord krxStockRecord: krxStockRecords) {
            Company company = companies.stream()
                    .filter(o -> o.getCompanyCode().equals(krxStockRecord.getCompanyCode()))
                    .findAny()
                    .orElse(null);
            if (company == null) {
                company = new Company(krxStockRecord, MarketType.KOSPI);
                newCompanies.add(company);
            }
            newStockRecords.add(new StockRecord(company, krxStockRecord, searchDate));
        }
        companyRepository.saveAll(newCompanies);
        stockRecordRepository.saveAll(newStockRecords);
        fetchRecordRepository.save(new FetchRecord(searchDate, LocalDateTime.now()));
    }
}