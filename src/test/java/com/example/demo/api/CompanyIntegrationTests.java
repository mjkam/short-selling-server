package com.example.demo.api;

import com.example.demo.api.builder.CompanyBuilder;
import com.example.demo.controller.dto.CompanyDto;
import com.example.demo.controller.dto.GetCompaniesResponse;
import com.example.demo.domain.Company;
import com.example.demo.domain.MarketType;
import com.example.demo.repository.CompanyRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CompanyIntegrationTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CompanyRepository companyRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private CompanyBuilder companyBuilder;

    @BeforeEach
    void setup() {
        companyRepository.deleteAll();

        companyBuilder = CompanyBuilder.company()
                .name("")
                .logoImageName("")
                .marketType(MarketType.KOSPI);
    }

    @AfterEach
    void teardown() {
        companyRepository.deleteAll();
    }

    @Test
    void getAllCompanies() throws Exception {
        //given
        companyRepository.save(company("company1"));
        companyRepository.save(company("company2"));

        //when
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/companies"))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        //then
        List<CompanyDto> companies = objectMapper.readValue(response.getContentAsString(), GetCompaniesResponse.class).getCompanies();
        assertThat(companies.size()).isEqualTo(2);
        assertContainsCompanyCodes(companies, List.of("company1", "company2"));
    }

    private Company company(String companyCode) {
        return companyBuilder.but()
                .companyCode(companyCode)
                .build();
    }

    private void assertContainsCompanyCodes(List<CompanyDto> companies, List<String> companyCodes) {
        boolean existCompanyCodesInCompanies = companies.stream()
                .allMatch(company -> companyCodes.contains(company.getCompanyCode()));
        assertThat(existCompanyCodesInCompanies).isTrue();
    }
}




