package com.example.demo.api;

import com.example.demo.AbstractIntegrationTest;
import com.example.demo.controller.dto.GetTop50Response;
import com.example.demo.domain.Company;
import com.example.demo.domain.StockRecord;
import com.example.demo.repository.CompanyRepository;
import com.example.demo.repository.FetchRecordRepository;
import com.example.demo.repository.StockRecordRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.example.demo.api.builder.CompanyBuilder.company;
import static com.example.demo.api.builder.FetchRecordBuilder.fetchRecord;
import static com.example.demo.api.builder.StockRecordBuilder.stockRecord;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class Top50IntegrationTests extends AbstractIntegrationTest {
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
