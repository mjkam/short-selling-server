package com.example.demo.api;

import com.example.demo.AbstractIntegrationTest;
import com.example.demo.BaseTest;
import com.example.demo.api.builder.CompanyBuilder;
import com.example.demo.api.builder.FetchRecordBuilder;
import com.example.demo.api.builder.StockRecordBuilder;
import com.example.demo.controller.Top50Controller;
import com.example.demo.controller.dto.GetStockRecordsResponse;
import com.example.demo.controller.dto.GetTop50Response;
import com.example.demo.controller.dto.StockRecordDto;
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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.example.demo.TimeUtils.*;
import static com.example.demo.api.builder.CompanyBuilder.*;
import static com.example.demo.api.builder.FetchRecordBuilder.*;
import static com.example.demo.api.builder.StockRecordBuilder.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
public class Top50Tests extends AbstractIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private StockRecordRepository stockRecordRepository;
    @Autowired
    private FetchRecordRepository fetchRecordRepository;

    @BeforeEach
    void setup() {
        cleanDB();
    }

    @AfterEach
    void cleanup() {
        cleanDB();
    }

    void cleanDB() {
        companyRepository.deleteAll();
        stockRecordRepository.deleteAll();
        fetchRecordRepository.deleteAll();
    }


    @Test
    @DisplayName("fetchRecord 가 없으면 Bad Request 리턴")
    void returnBadRequest_whenFetchRecordIsNotExist() throws Exception {
        //when
        mockMvc.perform(MockMvcRequestBuilders.get("/top50"))
                        .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("검색 결과 StockRecord 가 50개가 안되면 BadRequest 리턴")
    void returnBadRequest_whenStockRecordIsLessThan50() throws Exception {
        //given
        LocalDate currentDate = LocalDate.now();
        fetchRecordRepository.save(fetchRecord()
                .stockRecordDate(currentDate)
                .build());
        Company company = companyRepository.save(company()
                .companyCode("000000")
                .build());
        stockRecordRepository.save(stockRecord()
                .company(company)
                .recordDate(currentDate)
                .build());

        //when then
        mockMvc.perform(MockMvcRequestBuilders.get("/top50"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("50개의 StockRecord 를 공매도 비율의 내림차순으로 리턴")
    void return50StockRecords() throws Exception {
        //given
        LocalDate currentDate = LocalDate.now();
        LocalDate prevDate = currentDate.minusDays(1L);
        int recordCount = 100;

        fetchRecordRepository.save(fetchRecord().stockRecordDate(prevDate).build());
        fetchRecordRepository.save(fetchRecord().stockRecordDate(currentDate).build());

        List<Company> companies = saveCompanies(recordCount);
        saveStockRecords(companies, prevDate);
        saveStockRecords(companies, currentDate);

        //when then
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/top50"))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        //then
        List<GetTop50Response.Top50ListItem> resultTop50Records = objectMapper.readValue(
                response.getContentAsString(), GetTop50Response.class).getTop50Records();
        assertThat(resultTop50Records.size()).isEqualTo(50);
        assertThatOrderedByShortSellingRatioDesc(resultTop50Records);
    }

    private List<Company> saveCompanies(int count) {
        List<Company> companies = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            companies.add(company()
                    .companyCode(String.valueOf(i))
                    .build());
        }
        return companyRepository.saveAll(companies);
    }

    private List<StockRecord> saveStockRecords(List<Company> companies, LocalDate recordDate) {
        List<StockRecord> result = new ArrayList<>();
        for (int i = 0; i < companies.size(); i++) {
            result.add(stockRecord()
                    .company(companies.get(i))
                    .recordDate(recordDate)
                    .shortSellingRatio((float) i).build());
        }
        return stockRecordRepository.saveAll(result);
    }

    private void assertThatOrderedByShortSellingRatioDesc(List<GetTop50Response.Top50ListItem> top50ListItems) {
        for (int i = 1; i < top50ListItems.size(); i++) {
            assertThat(top50ListItems.get(i - 1).getShortSellingRatio()).isGreaterThanOrEqualTo(top50ListItems.get(i).getShortSellingRatio());
        }
    }
}
