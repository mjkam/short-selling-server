package com.example.demo.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

@NoArgsConstructor
@Getter
public class RegisterFavoriteRequest {
    @NotEmpty
    @Length(min = 6, max = 10)
    private String companyCode;

    @Length(min = 5, max = 11)
    private String name;
}
