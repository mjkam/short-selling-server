package com.example.demo.domain;

import com.example.demo.Constants;
import com.example.demo.cron.KRXStockRecord;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "company")
@NoArgsConstructor
@Getter
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "company_code")
    private String companyCode;

    @Column(name = "name")
    private String name;

    @Column(name = "market_type")
    @Enumerated(EnumType.STRING)
    private MarketType marketType;

    @Column(name = "logo_image_name")
    private String logoImageName;

    public Company(KRXStockRecord krxStockRecord, MarketType marketType) {
        this.companyCode = krxStockRecord.getCompanyCode();
        this.name = krxStockRecord.getCompanyName();
        this.marketType = marketType;
        this.logoImageName = Constants.defaultLogoImageName;
    }
}