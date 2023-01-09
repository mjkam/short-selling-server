package com.example.demo.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "stock_record")
@NoArgsConstructor
@Getter
public class StockRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "company_id")
    private Long companyId;

    @Column(name = "record_date")
    private LocalDate recordDate;

    @Column(name = "short_selling_share_count")
    private Long shortSellingShareCount;

    @Column(name = "listed_share_count")
    private Long listedShareCount;

    @Column(name = "short_selling_amount")
    private Long shortSellingAmount;

    @Column(name = "listed_share_amount")
    private Long listedShareAmount;

    @Column(name = "short_selling_ratio")
    private Float shortSellingRatio;
}
