package com.example.demo.api;

import com.example.demo.api.builder.CompanyBuilder;
import com.example.demo.controller.dto.GetCompaniesResponse;
import com.example.demo.domain.Company;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
public class CompanyIntegrationTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CompanyRepository companyRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    @AfterEach
    @BeforeEach
    void setup() {
        companyRepository.deleteAll();
    }

    @Test
    void getAllCompanies() throws Exception {
        //given
        Company company1 = CompanyBuilder.company()
                .companyCode("1")
                .logoImageName("")
                .build();
        Company company2 = CompanyBuilder.company()
                .companyCode("2")
                .logoImageName("")
                .build();
        companyRepository.saveAll(List.of(company1, company2));

        //when
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/companies"))
                .andReturn()
                .getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        GetCompaniesResponse getCompaniesResponse = objectMapper.readValue(response.getContentAsString(), GetCompaniesResponse.class);
        assertThat(getCompaniesResponse.getCompanies().size()).isEqualTo(2);
    }
}
