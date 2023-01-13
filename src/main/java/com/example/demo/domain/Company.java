package com.example.demo.domain;

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

    @Column(name = "stock_code")
    private String stockCode;

    @Column(name = "name")
    private String name;

    @Column(name = "market_type")
    @Enumerated(EnumType.STRING)
    private MarketType marketType;

    @Column(name = "logo_image_name")
    private String logoImageName;
}