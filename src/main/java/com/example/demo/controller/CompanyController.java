package com.example.demo.controller;

import com.example.demo.controller.dto.GetCompaniesResponse;
import com.example.demo.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyService companyService;

    @GetMapping("/companies")
    public GetCompaniesResponse getAllCompanies() {
        return new GetCompaniesResponse(companyService.getAllCompanies());
    }
}
