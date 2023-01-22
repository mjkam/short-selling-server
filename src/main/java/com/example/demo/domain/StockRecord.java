package com.example.demo.domain;

import com.example.demo.cron.KRXStockRecord;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Locale;

@Entity
@Table(name = "stock_record")
@NoArgsConstructor
@Getter
public class StockRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

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

    public StockRecord(Company company, KRXStockRecord krxStockRecord, LocalDate recordDate) {
        this.company = company;
        this.recordDate = recordDate;
        this.shortSellingShareCount = krxStockRecord.getShortSellingShareCount();
        this.listedShareCount = krxStockRecord.getListedShareCount();
        this.shortSellingAmount = krxStockRecord.getShortSellingAmount();
        this.listedShareAmount = krxStockRecord.getListedShareAmount();
        this.shortSellingRatio = krxStockRecord.getShortSellingShareRatio();
    }
}
