package com.example.demo.api.builder;

import com.example.demo.TimeUtils;
import com.example.demo.domain.FetchRecord;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;

public final class FetchRecordBuilder {
    private Long id;
    private LocalDate stockRecordDate;
    private LocalDateTime executedDateTime = TimeUtils.localDateTime("2022-10-10 00:00:00");

    private FetchRecordBuilder() {
    }

    public static FetchRecordBuilder fetchRecord() {
        return new FetchRecordBuilder();
    }

    public FetchRecordBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public FetchRecordBuilder stockRecordDate(LocalDate stockRecordDate) {
        this.stockRecordDate = stockRecordDate;
        return this;
    }

    public FetchRecordBuilder but() {
        return fetchRecord().id(id).stockRecordDate(stockRecordDate);
    }

    public FetchRecord build() {
        FetchRecord fetchRecord = new FetchRecord();
        ReflectionTestUtils.setField(fetchRecord, "id", this.id);
        ReflectionTestUtils.setField(fetchRecord, "stockRecordDate", this.stockRecordDate);
        ReflectionTestUtils.setField(fetchRecord, "executedDateTime", this.executedDateTime);

        return fetchRecord;
    }
}
