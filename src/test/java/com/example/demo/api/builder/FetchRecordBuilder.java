package com.example.demo.api.builder;

import com.example.demo.domain.FetchRecord;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FetchRecordBuilder {
    private Long id = 1L;
    private LocalDate stockRecordDate;
    private LocalDateTime executedDateTime = LocalDateTime.parse("2022-01-01 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

    private FetchRecordBuilder() {}

    public static FetchRecordBuilder fetchRecord() {
        return new FetchRecordBuilder();
    }

    public FetchRecordBuilder stockRecordDate(LocalDate localDate) {
        this.stockRecordDate = localDate;
        return this;
    }

    public FetchRecord build() {
        return new FetchRecord(this.stockRecordDate, this.executedDateTime);
    }
}

