package com.example.demo.api;

import com.example.demo.TimeManager;
import com.example.demo.api.builder.CompanyBuilder;
import com.example.demo.api.builder.StockRecordBuilder;
import com.example.demo.domain.Company;
import com.example.demo.domain.MarketType;
import com.example.demo.domain.StockRecord;
import com.example.demo.repository.CompanyRepository;
import com.example.demo.repository.StockRecordRepository;
import com.example.demo.service.StockRecordService;
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

@SpringBootTest
public class StockRecordServiceTests {
    @Autowired
    private StockRecordService sut;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private StockRecordRepository stockRecordRepository;
    @MockBean
    private TimeManager timeManager;

    private CompanyBuilder companyBuilder;
    private StockRecordBuilder stockRecordBuilder;


    @BeforeEach
    void setup() {
        companyRepository.deleteAll();
        stockRecordRepository.deleteAll();

        companyBuilder = CompanyBuilder.company()
                .name("")
                .logoImageName("")
                .marketType(MarketType.KOSPI);
        stockRecordBuilder = StockRecordBuilder.stockRecord()
                .listedShareCount(0L)
                .shortSellingAmount(0L)
                .shortSellingShareCount(0L)
                .listedShareAmount(0L)
                .shortSellingRatio(0f);
    }

    @AfterEach
    void teardown() {
        companyRepository.deleteAll();
        stockRecordRepository.deleteAll();
    }

    private Company company(String companyCode) {
        return companyBuilder.but()
                .companyCode(companyCode)
                .build();
    }

    private StockRecord stockRecord(Company company, LocalDate localDate) {
        return stockRecordBuilder.but()
                .company(company)
                .recordDate(localDate)
                .build();
    }

    @Test
    @DisplayName("StockRecord 데이터 가져오기")
    void getStockRecords() {
        //given
        given(timeManager.getCurrentDate()).willReturn(localDate("2022-10-13"));

        Company company1 = companyRepository.save(company("company1"));
        stockRecordRepository.save(stockRecord(company1, localDate("2022-10-13")));
        stockRecordRepository.save(stockRecord(company1, localDate("2022-10-12")));
        stockRecordRepository.save(stockRecord(company1, localDate("2022-10-11")));
        stockRecordRepository.save(stockRecord(company1, localDate("2022-10-10")));

        Company company2 = companyRepository.save(company("company2"));
        stockRecordRepository.save(stockRecord(company2, localDate("2022-10-10")));

        //when
        List<StockRecord> result = sut.getStockRecords("company1", 3);

        //then
        assertThat(result.size()).isEqualTo(4);
        assertThat(result.get(0).getRecordDate()).isEqualTo(localDate("2022-10-13").toString());
        assertThat(result.get(1).getRecordDate()).isEqualTo(localDate("2022-10-12").toString());
        assertThat(result.get(2).getRecordDate()).isEqualTo(localDate("2022-10-11").toString());
        assertThat(result.get(3).getRecordDate()).isEqualTo(localDate("2022-10-10").toString());
    }
}