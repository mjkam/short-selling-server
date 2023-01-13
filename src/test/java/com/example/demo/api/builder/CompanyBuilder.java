package com.example.demo.api.builder;

import com.example.demo.domain.Company;
import com.example.demo.domain.MarketType;
import org.springframework.test.util.ReflectionTestUtils;

public class CompanyBuilder {
    private Long id;
    private String companyCode = "";
    private String stockCode = "";
    private String name = "";
    private MarketType marketType = MarketType.KOSPI;
    private String logoImageName = "";

    private CompanyBuilder() {}

    public static CompanyBuilder company() {
        return new CompanyBuilder();
    }

    public CompanyBuilder logoImageName(String s) {
        this.logoImageName = s;
        return this;
    }

    public Company build() {
        Company company = new Company();
        ReflectionTestUtils.setField(company, "logoImageName", this.logoImageName);

        return company;
    }
}


