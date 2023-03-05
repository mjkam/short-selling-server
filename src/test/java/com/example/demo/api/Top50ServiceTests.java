package com.example.demo.api;

import com.example.demo.AbstractIntegrationTest;
import com.example.demo.api.builder.CompanyBuilder;
import com.example.demo.api.builder.FetchRecordBuilder;
import com.example.demo.api.builder.StockRecordBuilder;
import com.example.demo.domain.Company;
import com.example.demo.domain.FetchRecord;
import com.example.demo.domain.MarketType;
import com.example.demo.domain.StockRecord;
import com.example.demo.repository.CompanyRepository;
import com.example.demo.repository.FetchRecordRepository;
import com.example.demo.repository.StockRecordRepository;
import com.example.demo.service.Top50Service;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.example.demo.TimeUtils.localDate;
import static com.example.demo.TimeUtils.localDateTime;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class Top50ServiceTests extends AbstractIntegrationTest {
    @Autowired
    private Top50Service sut;
    @Autowired
    private StockRecordRepository stockRecordRepository;
    @Autowired
    private FetchRecordRepository fetchRecordRepository;
    @Autowired
    private CompanyRepository companyRepository;

    private StockRecordBuilder stockRecordBuilder;
    private CompanyBuilder companyBuilder;

    @BeforeEach
    void setup() {
        companyRepository.deleteAll();
        stockRecordRepository.deleteAll();
        fetchRecordRepository.deleteAll();

        stockRecordBuilder = StockRecordBuilder.stockRecord()
                .listedShareCount(0L)
                .shortSellingAmount(0L)
                .shortSellingShareCount(0L)
                .listedShareAmount(0L)
                .shortSellingRatio(0f);
        companyBuilder = CompanyBuilder.company()
                .name("")
                .logoImageName("")
                .marketType(MarketType.KOSPI);
    }

    @AfterEach
    void cleanup() {
        companyRepository.deleteAll();
        stockRecordRepository.deleteAll();
        fetchRecordRepository.deleteAll();
    }

    @Test
    @DisplayName("fetchRecord 가 없으면 IllegalStateException 발생")
    void throwIllegalStateException_whenFetchRecordNotExists() {
        assertThatThrownBy(() -> sut.getTop50())
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("검색 결과 StockRecord 가 50개가 안되면 IllegalStateException 발생")
    void throwIllegalStateException_whenStockRecordsSizeIsLessThan50() {
        //given
        saveFetchRecord(localDate("2022-10-10"));
        Company company = companyRepository.save(companyBuilder.but().companyCode("000000").build());
        stockRecordRepository.save(stockRecordBuilder.but()
                .company(company)
                .recordDate(localDate("2022-10-10"))
                .build());

        //when then
        assertThatThrownBy(() -> sut.getTop50())
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void requestTop50Test() {
        //given
        int recordCount = 100;
        List<Company> companies = saveCompanies(recordCount);
        saveStockRecords(companies, localDate("2022-10-09"), recordCount);
        saveStockRecords(companies, localDate("2022-10-10"), recordCount);

        saveFetchRecord(localDate("2022-10-09"));
        saveFetchRecord(localDate("2022-10-10"));

        //when
        List<StockRecord> top50StockRecords = sut.getTop50();

        //then
        assertThat(top50StockRecords.size()).isEqualTo(50);
        assertThatStockRecordsOrderedByShortSellingRatioDesc(top50StockRecords);
    }

    private FetchRecord saveFetchRecord(LocalDate localDate) {
        return fetchRecordRepository.save(FetchRecordBuilder.fetchRecord()
                .stockRecordDate(localDate)
                .build());
    }

    private List<Company> saveCompanies(int count) {
        List<Company> companies = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            companies.add(companyBuilder.but()
                    .companyCode(String.valueOf(i))
                    .build());
        }
        return companyRepository.saveAll(companies);
    }

    private List<StockRecord> saveStockRecords(List<Company> companies, LocalDate recordDate, int count) {
        List<StockRecord> result = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            result.add(stockRecordBuilder.but()
                    .company(companies.get(i))
                    .recordDate(recordDate)
                    .shortSellingRatio((float) i).build());
        }
        return stockRecordRepository.saveAll(result);
    }

    private void assertThatStockRecordsOrderedByShortSellingRatioDesc(List<StockRecord> top50ListItems) {
        for (int i = 1; i < top50ListItems.size(); i++) {
            assertThat(top50ListItems.get(i - 1).getShortSellingRatio()).isGreaterThanOrEqualTo(top50ListItems.get(i).getShortSellingRatio());
        }
    }
}