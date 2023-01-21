package com.example.demo.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "favorite_record")
@NoArgsConstructor
@Getter
public class FavoriteRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "company_code")
    private String companyCode;

    @Column(name = "count")
    private Integer count;

    public FavoriteRecord(String companyCode) {
        this.companyCode = companyCode;
        this.count = 0;
    }

    public void incrementCount() {
        this.count++;
    }
}
