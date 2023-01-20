package com.example.demo.api;

import com.example.demo.TimeManager;
import com.example.demo.api.builder.CompanyBuilder;
import com.example.demo.api.builder.StockRecordBuilder;
import com.example.demo.controller.dto.GetStockRecordsResponse;
import com.example.demo.controller.dto.StockRecordDto;
import com.example.demo.domain.Company;
import com.example.demo.domain.MarketType;
import com.example.demo.domain.StockRecord;
import com.example.demo.repository.CompanyRepository;
import com.example.demo.repository.StockRecordRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static com.example.demo.TimeUtils.localDate;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class GetStockRecordsIntegrationTests {
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private StockRecordRepository stockRecordRepository;
    @MockBean
    private TimeManager timeManager;
    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

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
    void getStockRecords() throws Exception {
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
        MockHttpServletResponse response = mockMvc
                .perform(get(String.format("/company/%s/stock-records?duration=%d", "company1", 3)))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        //then
        List<StockRecordDto> stockRecords = objectMapper.readValue(response.getContentAsString(), GetStockRecordsResponse.class).getStockRecords();

        assertThat(stockRecords.size()).isEqualTo(4);
        assertThat(stockRecords.get(0).getRecordDate()).isEqualTo(localDate("2022-10-13").toString());
        assertThat(stockRecords.get(1).getRecordDate()).isEqualTo(localDate("2022-10-12").toString());
        assertThat(stockRecords.get(2).getRecordDate()).isEqualTo(localDate("2022-10-11").toString());
        assertThat(stockRecords.get(3).getRecordDate()).isEqualTo(localDate("2022-10-10").toString());
    }
}