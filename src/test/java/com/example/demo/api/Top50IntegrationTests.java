package com.example.demo.api;

import com.example.demo.api.builder.CompanyBuilder;
import com.example.demo.api.builder.StockRecordBuilder;
import com.example.demo.controller.dto.GetTop50Response;
import com.example.demo.domain.Company;
import com.example.demo.domain.StockRecord;
import com.example.demo.repository.CompanyRepository;
import com.example.demo.repository.FetchRecordRepository;
import com.example.demo.repository.StockRecordRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.example.demo.TimeUtils.localDate;
import static com.example.demo.api.builder.FetchRecordBuilder.fetchRecord;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
public class Top50IntegrationTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private StockRecordRepository stockRecordRepository;
    @Autowired
    private FetchRecordRepository fetchRecordRepository;
    @Autowired
    private CompanyRepository companyRepository;

    @BeforeEach
    void setup() {
        companyRepository.deleteAll();
        stockRecordRepository.deleteAll();
        fetchRecordRepository.deleteAll();
    }

    @AfterEach
    void cleanup() {
        companyRepository.deleteAll();
        stockRecordRepository.deleteAll();
        fetchRecordRepository.deleteAll();
    }

    @Test
    void requestTop50Test() throws Exception {
        //given
        List<Company> companies = companyRepository.saveAll(createCompanies());
        stockRecordRepository.saveAll(createStockRecords(companies, localDate("2022-10-09"), 50));
        stockRecordRepository.saveAll(createStockRecords(companies, localDate("2022-10-10"), 50));

        fetchRecordRepository.save(fetchRecord()
                .stockRecordDate(localDate("2022-10-09"))
                .build());
        fetchRecordRepository.save(fetchRecord()
                .stockRecordDate(localDate("2022-10-10"))
                .build());

        //when
        MockHttpServletResponse response = mockMvc.perform(get("/top50")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        GetTop50Response getTop50Response = objectMapper.readValue(response.getContentAsString(), GetTop50Response.class);

        assertThat(getTop50Response.getTop50Records().size()).isEqualTo(50);
        assertThat(getTop50Response.getRecordDate()).isEqualTo("2022-10-10");
        assertThatStockRecordsDesc(getTop50Response.getTop50Records());
    }

    private List<Company> createCompanies() {
        List<Company> companies = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            companies.add(CompanyBuilder.company().logoImageName(String.valueOf(i)).build());
        }
        return companies;
    }

    private List<StockRecord> createStockRecords(List<Company> companies, LocalDate recordDate, int count) {
        List<StockRecord> result = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            result.add(StockRecordBuilder.stockRecord()
                    .company(companies.get(i))
                    .recordDate(recordDate)
                    .shortSellingRatio((float) i).build());
        }
        return result;
    }

    private void assertThatStockRecordsDesc(List<GetTop50Response.Top50ListItem> top50ListItems) {
        for (int i = 1; i < top50ListItems.size(); i++) {
            assertThat(top50ListItems.get(i - 1).getShortSellingRatio()).isGreaterThanOrEqualTo(top50ListItems.get(i).getShortSellingRatio());
        }
    }
}
