package com.example.demo.controller;

import com.example.demo.controller.dto.GetCompaniesResponse;
import com.example.demo.domain.Company;
import com.example.demo.service.CompanyService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyService companyService;

    @GetMapping("/companies")
    public GetCompaniesResponse getAllCompanies() {
        return new GetCompaniesResponse(companyService.getAllCompanies());
    }
}
