package com.example.demo.controller.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class GetTop50Response {
    private List<Object> records;
    private String recordDate;
}