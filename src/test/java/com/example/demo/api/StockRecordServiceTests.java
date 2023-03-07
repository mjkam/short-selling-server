package com.example.demo.api;

import com.example.demo.AbstractIntegrationTest;
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
import com.example.demo.service.StockRecordService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.List;

import static com.example.demo.TimeUtils.localDate;
import static com.example.demo.api.builder.CompanyBuilder.*;
import static com.example.demo.api.builder.StockRecordBuilder.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class StockRecordServiceTests extends AbstractIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private StockRecordRepository stockRecordRepository;
    @MockBean
    private TimeManager timeManager;

    @BeforeEach
    void setup() {
        cleanDB();
    }

    @AfterEach
    void teardown() {
        cleanDB();
    }

    void cleanDB() {
        companyRepository.deleteAll();
        stockRecordRepository.deleteAll();
    }

    @Test
    @DisplayName("StockRecord 데이터 가져오기")
    void getStockRecords() throws Exception {
        //given
        given(timeManager.getCurrentDate()).willReturn(localDate("2022-10-13"));

        Company company1 = companyRepository.save(company().companyCode("company1").build());
        StockRecord stockRecord1 = stockRecord()
                .company(company1)
                .recordDate(localDate("2022-10-13"))
                .build();
        StockRecord stockRecord2 = stockRecord()
                .company(company1)
                .recordDate(localDate("2022-10-12"))
                .build();
        StockRecord stockRecord3 = stockRecord()
                .company(company1)
                .recordDate(localDate("2022-10-11"))
                .build();
        StockRecord stockRecord4 = stockRecord()
                .company(company1)
                .recordDate(localDate("2022-10-10"))
                .build();
        stockRecordRepository.saveAll(List.of(stockRecord1, stockRecord2, stockRecord3, stockRecord4));

        Company company2 = companyRepository.save(company().companyCode("company2").build());
        StockRecord stockRecord5 = stockRecord()
                .company(company2)
                .recordDate(localDate("2022-10-13"))
                .build();
        stockRecordRepository.save(stockRecord5);


        //when
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/company/company1/stock-records?duration=3"))
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