package com.example.demo.cron;

import com.example.demo.AbstractIntegrationTest;
import com.example.demo.api.builder.CompanyBuilder;
import com.example.demo.api.builder.KRXStockRecordBuilder;
import com.example.demo.domain.Company;
import com.example.demo.domain.FetchRecord;
import com.example.demo.domain.MarketType;
import com.example.demo.domain.StockRecord;
import com.example.demo.repository.CompanyRepository;
import com.example.demo.repository.FetchRecordRepository;
import com.example.demo.repository.StockRecordRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.List;

import static com.example.demo.TimeUtils.localDate;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

public class StockRecordsSaverTests extends AbstractIntegrationTest {
    private StockRecordsSaver sut;

    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private StockRecordRepository stockRecordRepository;
    @Autowired
    private FetchRecordRepository fetchRecordRepository;

    @MockBean
    private KRXApi krxApi;

    private KRXStockRecordBuilder krxStockRecordBuilder;
    private CompanyBuilder companyBuilder;

    @BeforeEach
    void setup() {
        companyRepository.deleteAll();;
        stockRecordRepository.deleteAll();
        fetchRecordRepository.deleteAll();

        krxStockRecordBuilder = KRXStockRecordBuilder.krxStockRecord()
                .companyName("")
                .shortSellingShareCount("0")
                .shortSellingAmount("0")
                .listedShareAmount("0")
                .listedShareCount("0")
                .shortSellingShareRatio("0");

        companyBuilder = CompanyBuilder.company()
                .name("")
                .logoImageName("");

        sut = new StockRecordsSaver(
                companyRepository, stockRecordRepository, fetchRecordRepository, krxApi);
    }

    private KRXStockRecord krxStockRecord(String companyCode) {
        return krxStockRecordBuilder.but()
                .companyCode(companyCode).build();
    }

    private Company saveCompany(String companyCode, MarketType marketType) {
        return companyRepository.save(companyBuilder.but()
                .companyCode(companyCode)
                .marketType(marketType)
                .build());
    }

    @AfterEach
    void teardown() {
        companyRepository.deleteAll();;
        stockRecordRepository.deleteAll();
        fetchRecordRepository.deleteAll();
    }

    @Test
    @DisplayName("가져온 KRXStockRecord 를 StockRecord 로 저장")
    void saveStockRecordsFromKRXStockRecords() {
        //given
        LocalDate givenDate = localDate("2022-10-12");

        String companyCode1 = "000001";
        String companyCode2 = "000002";
        KRXStockRecord krxStockRecord1 = krxStockRecord(companyCode1);
        KRXStockRecord krxStockRecord2 = krxStockRecord(companyCode2);

        given(krxApi.getStockRecordsAt(givenDate, MarketType.KOSPI))
                .willReturn(List.of(krxStockRecord1));
        given(krxApi.getStockRecordsAt(givenDate, MarketType.KOSDAQ))
                .willReturn(List.of(krxStockRecord2));

        Company company1 = saveCompany(companyCode1, MarketType.KOSPI);
        Company company2 = saveCompany(companyCode2, MarketType.KOSDAQ);

        //when
        sut.save(givenDate);

        //then
        assertStockRecordExist(company1, givenDate);
        assertStockRecordExist(company2, givenDate);
    }

    private void assertStockRecordExist(Company company, LocalDate stockRecordDate) {
        StockRecord stockRecord = stockRecordRepository.findAll().stream()
                .filter(o -> o.getCompany().getId().equals(company.getId()))
                .findAny()
                .orElse(null);
        assertThat(stockRecord).isNotNull();
        assertThat(stockRecord.getRecordDate()).isEqualTo(stockRecordDate);
    }



    @Test
    @DisplayName("KRXStockRecord 가 저장되는게 있으면 검색 날짜에 fetchRecord 저장")
    void saveFetchRecord_whenFetchedKRXStockRecordExist() {
        //given
        LocalDate givenDate = localDate("2022-10-12");

        String companyCode1 = "000001";
        KRXStockRecord krxStockRecord1 = krxStockRecord(companyCode1);

        given(krxApi.getStockRecordsAt(givenDate, MarketType.KOSPI))
                .willReturn(List.of(krxStockRecord1));

        //when
        sut.save(givenDate);

        //then
        List<FetchRecord> allFetchRecords = fetchRecordRepository.findAll();
        assertThat(allFetchRecords.size()).isEqualTo(1);
        assertThat(allFetchRecords.get(0).getStockRecordDate()).isEqualTo(givenDate);
    }

    @Test
    @DisplayName("코스피, 코스닥 데이터 구분해서 저장")
    void saveAllMarketTypeRecords() {
        //given
        LocalDate givenDate = localDate("2022-10-12");

        String companyCode1 = "000001";
        String companyCode2 = "000002";
        KRXStockRecord krxStockRecord1 = krxStockRecord(companyCode1);
        KRXStockRecord krxStockRecord2 = krxStockRecord(companyCode2);

        given(krxApi.getStockRecordsAt(givenDate, MarketType.KOSPI))
                .willReturn(List.of(krxStockRecord1));
        given(krxApi.getStockRecordsAt(givenDate, MarketType.KOSDAQ))
                .willReturn(List.of(krxStockRecord2));

        //when
        sut.save(givenDate);

        //then
        assertSaveCompanyMarketType(companyCode1, MarketType.KOSPI);
        assertSaveCompanyMarketType(companyCode2, MarketType.KOSDAQ);
    }

    private void assertSaveCompanyMarketType(String companyCode, MarketType marketType) {
        Company company = companyRepository.findByCompanyCode(companyCode).orElse(null);
        assertThat(company).isNotNull();
        assertThat(company.getMarketType()).isEqualTo(marketType);
    }

    @Test
    @DisplayName("KRX api 의 리턴 데이터중에 새로운 회사가 있으면 저장")
    void saveNewCompany() {
        //given
        LocalDate givenDate = localDate("2022-10-12");

        String companyCode1 = "000001";
        String companyCode2 = "000002";
        String companyCode3 = "000003";
        KRXStockRecord krxStockRecord1 = krxStockRecord(companyCode1);
        KRXStockRecord krxStockRecord2 = krxStockRecord(companyCode2);
        KRXStockRecord krxStockRecord3 = krxStockRecord(companyCode3);

        saveCompany(companyCode1, MarketType.KOSPI);
        saveCompany(companyCode2, MarketType.KOSPI);

        given(krxApi.getStockRecordsAt(givenDate, MarketType.KOSPI))
                .willReturn(List.of(krxStockRecord1, krxStockRecord2, krxStockRecord3));

        //when
        sut.save(givenDate);

        //then
        assertCompanyCodeSaved(companyCode3);
    }

    private void assertCompanyCodeSaved(String companyCode) {
        Company company = companyRepository.findByCompanyCode(companyCode).orElse(null);
        assertThat(company).isNotNull();
    }
}
