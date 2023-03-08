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
public class StockRecordsApplier {
    private final CompanyRepository companyRepository;
    private final StockRecordRepository stockRecordRepository;
    private final FetchRecordRepository fetchRecordRepository;

    private final KRXApi krxApi;

    @Transactional
    public void apply(LocalDate searchDate) {
        saveRecords(searchDate);
        fetchRecordRepository.save(new FetchRecord(searchDate, LocalDateTime.now()));
    }

    private void saveRecords(LocalDate searchDate) {
        List<KRXStockRecord> kospiKRXStockRecords = krxApi.getStockRecordsAt(searchDate, MarketType.KOSPI);
        List<KRXStockRecord> kosdaqKRXStockRecords = krxApi.getStockRecordsAt(searchDate, MarketType.KOSDAQ);

        List<Company> companies = companyRepository.findAll();
        List<Company> kospiCompanies = companyRepository.saveAll(createNotSavedCompanies(companies, kospiKRXStockRecords, MarketType.KOSPI));
        List<Company> kosdaqCompanies = companyRepository.saveAll(createNotSavedCompanies(companies, kosdaqKRXStockRecords, MarketType.KOSDAQ));

        companies.addAll(kospiCompanies);
        companies.addAll(kosdaqCompanies);

        List<KRXStockRecord> krxStockRecords = new ArrayList<>();
        krxStockRecords.addAll(kospiKRXStockRecords);
        krxStockRecords.addAll(kosdaqKRXStockRecords);

        List<StockRecord> stockRecords = krxStockRecords.stream()
                .map(krxStockRecord -> new StockRecord(findMatchCompany(companies, krxStockRecord), krxStockRecord, searchDate))
                .collect(Collectors.toList());
        stockRecordRepository.saveAll(stockRecords);
    }

    private Company findMatchCompany(List<Company> companies, KRXStockRecord krxStockRecord) {
        return companies.stream()
                .filter(o -> o.getCompanyCode().equals(krxStockRecord.getCompanyCode()))
                .findAny()
                .orElseThrow(() -> new IllegalStateException("Can not find company " + krxStockRecord.getCompanyCode()));
    }

    private List<Company> createNotSavedCompanies(List<Company> companies, List<KRXStockRecord> krxStockRecords, MarketType marketType) {
        List<String> companyCodes = companies.stream()
                .map(Company::getCompanyCode)
                .collect(Collectors.toList());
        return krxStockRecords.stream()
                .filter(krxStockRecord -> !companyCodes.contains(krxStockRecord.getCompanyCode()))
                .map(krxStockRecord -> new Company(krxStockRecord, marketType))
                .collect(Collectors.toList());
    }


}