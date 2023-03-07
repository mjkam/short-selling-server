package com.example.demo.api.builder;

import com.example.demo.domain.Company;
import com.example.demo.domain.MarketType;
import org.springframework.test.util.ReflectionTestUtils;

public final class CompanyBuilder {
    private Long id;
    private String companyCode;
    private String name = "";
    private MarketType marketType = MarketType.KOSPI;
    private String logoImageName = "";

    private CompanyBuilder() {
    }

    public static CompanyBuilder company() {
        return new CompanyBuilder();
    }

    public CompanyBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public CompanyBuilder companyCode(String companyCode) {
        this.companyCode = companyCode;
        return this;
    }

    public CompanyBuilder name(String name) {
        this.name = name;
        return this;
    }

    public CompanyBuilder marketType(MarketType marketType) {
        this.marketType = marketType;
        return this;
    }

    public CompanyBuilder logoImageName(String logoImageName) {
        this.logoImageName = logoImageName;
        return this;
    }

    public CompanyBuilder but() {
        return company().id(id).companyCode(companyCode).name(name).marketType(marketType).logoImageName(logoImageName);
    }

    public Company build() {
        Company company = new Company();
        ReflectionTestUtils.setField(company, "id", this.id);
        ReflectionTestUtils.setField(company, "companyCode", this.companyCode);
        ReflectionTestUtils.setField(company, "name", this.name);
        ReflectionTestUtils.setField(company, "marketType", this.marketType);
        ReflectionTestUtils.setField(company, "logoImageName", this.logoImageName);

        return company;
    }
}
