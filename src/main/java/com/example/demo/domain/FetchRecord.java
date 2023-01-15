package com.example.demo.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "fetch_record")
@NoArgsConstructor
@Getter
public class FetchRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "stock_record_date")
    private LocalDate stockRecordDate;

    @Column(name = "executed_datetime")
    private LocalDateTime executedDateTime;

    public FetchRecord(LocalDate stockRecordDate, LocalDateTime executedDateTime) {
        this.stockRecordDate = stockRecordDate;
        this.executedDateTime = executedDateTime;
    }

    public LocalDate getStockRecordDate() {
        return this.stockRecordDate;
    }
    public LocalDate getNextStockRecordDate() {
        return this.stockRecordDate.plusDays(1);
    }
}
