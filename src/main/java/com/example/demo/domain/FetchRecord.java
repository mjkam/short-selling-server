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

    private LocalDate stockRecordDate;

    public LocalDate getStockRecordDate() {
        return this.stockRecordDate;
    }
}
