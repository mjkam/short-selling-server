package com.example.demo.api;

import com.example.demo.AbstractIntegrationTest;
import com.example.demo.controller.dto.CompanyDto;
import com.example.demo.controller.dto.GetCompaniesResponse;
import com.example.demo.domain.Company;
import com.example.demo.repository.CompanyRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.demo.api.builder.CompanyBuilder.company;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class CompanyIntegrationTests extends AbstractIntegrationTest {
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    private CompanyRepository companyRepository;

    @BeforeEach
    void setup() {
        companyRepository.deleteAll();
    }

    @AfterEach
    void teardown() {
        companyRepository.deleteAll();
    }

    @Test
    @DisplayName("회사 데이터 전체 가져오기")
    void getAllCompanies() throws Exception {
        //given
        Company company1 = company().companyCode("company1").build();
        Company company2 = company().companyCode("company2").build();
        companyRepository.save(company1);
        companyRepository.save(company2);

        //when
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/companies"))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        //then
        List<CompanyDto> resultCompanies = objectMapper.readValue(response.getContentAsString(), GetCompaniesResponse.class).getCompanies();
        assertThat(resultCompanies.size()).isEqualTo(List.of(company1, company2).size());
        assertThatResultHaveCompanies(resultCompanies, List.of(company1, company2));
    }

    private void assertThatResultHaveCompanies(List<CompanyDto> resultCompanies, List<Company> savedCompanies) {
        List<String> savedCompanyCodes = savedCompanies.stream()
                .map(Company::getCompanyCode)
                .collect(Collectors.toList());

        boolean existCompanyCodesInCompanies = resultCompanies.stream()
                .allMatch(company -> savedCompanyCodes.contains(company.getCompanyCode()));
        assertThat(existCompanyCodesInCompanies).isTrue();
    }
}




