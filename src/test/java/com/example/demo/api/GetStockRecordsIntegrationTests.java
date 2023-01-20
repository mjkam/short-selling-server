package com.example.demo.api;

import com.example.demo.TimeManager;
import com.example.demo.api.builder.CompanyBuilder;
import com.example.demo.api.builder.StockRecordBuilder;
import com.example.demo.controller.dto.GetStockRecordsResponse;
import com.example.demo.controller.dto.StockRecordDto;
import com.example.demo.domain.Company;
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
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.example.demo.TimeUtils.localDate;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

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

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    @AfterEach
    void init() {
        companyRepository.deleteAll();
        stockRecordRepository.deleteAll();
    }

    @Test
    void getStockRecords() throws Exception {
        //given
        given(timeManager.getCurrentDate()).willReturn(localDate("2022-10-13"));

        Company company = CompanyBuilder.company()
                .companyCode("123")
                .logoImageName("")
                .build();
        companyRepository.save(company);

        StockRecord stockRecord1 = StockRecordBuilder.stockRecord()
                .company(company)
                .shortSellingRatio(1.0f)
                .recordDate(localDate("2022-10-12"))
                .build();
        StockRecord stockRecord2 = StockRecordBuilder.stockRecord()
                .company(company)
                .shortSellingRatio(1.0f)
                .recordDate(localDate("2022-10-11"))
                .build();
        StockRecord stockRecord3 = StockRecordBuilder.stockRecord()
                .company(company)
                .shortSellingRatio(1.0f)
                .recordDate(localDate("2022-10-10"))
                .build();
        StockRecord stockRecord4 = StockRecordBuilder.stockRecord()
                .company(company)
                .shortSellingRatio(1.0f)
                .recordDate(localDate("2022-10-09"))
                .build();
        stockRecordRepository.saveAll(List.of(stockRecord1, stockRecord2, stockRecord3, stockRecord4));


        //when
        MockHttpServletResponse response = mockMvc
                .perform(get(String.format("/company/%s/stock-records?duration=%d", "123", 3)))
                .andReturn().getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        GetStockRecordsResponse getStockRecordsResponse = objectMapper.readValue(response.getContentAsString(), GetStockRecordsResponse.class);
        List<StockRecordDto> stockRecords = getStockRecordsResponse.getStockRecords();


        assertThat(stockRecords.size()).isEqualTo(3);
        assertThat(stockRecords.get(0).getRecordDate()).isEqualTo(localDate("2022-10-10").toString());
        assertThat(stockRecords.get(1).getRecordDate()).isEqualTo(localDate("2022-10-11").toString());
        assertThat(stockRecords.get(2).getRecordDate()).isEqualTo(localDate("2022-10-12").toString());
    }
}
