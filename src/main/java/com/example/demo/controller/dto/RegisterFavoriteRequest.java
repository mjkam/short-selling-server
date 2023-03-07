package com.example.demo.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@NoArgsConstructor
@Getter
public class RegisterFavoriteRequest {
    @NotEmpty
    private String companyCode;
}
