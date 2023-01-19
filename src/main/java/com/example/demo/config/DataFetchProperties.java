package com.example.demo.config;

import com.example.demo.Constants;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@ConfigurationProperties(prefix = "app")
public class DataFetchProperties {
    private String fetchStartDate;

    public LocalDate getFetchStartDate() {
        return LocalDate.parse(this.fetchStartDate, Constants.dateFormatter);
    }
}