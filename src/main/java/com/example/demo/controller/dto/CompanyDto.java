package com.example.demo.controller.dto;

import com.example.demo.domain.Company;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CompanyDto {
    private String companyCode;
    private String companyName;
    private String logoImageName;

    public CompanyDto(Company company) {
        this.companyCode = company.getCompanyCode();
        this.companyName = company.getName();
        this.logoImageName = company.getLogoImageName();
    }
}
