package com.example.demo.controller.dto;

import com.example.demo.domain.Company;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class GetCompaniesResponse {
    private List<CompanyDto> companies;

    public GetCompaniesResponse(List<Company> companies) {
        this.companies = companies.stream()
                .map(CompanyDto::new)
                .collect(Collectors.toList());
    }
}
