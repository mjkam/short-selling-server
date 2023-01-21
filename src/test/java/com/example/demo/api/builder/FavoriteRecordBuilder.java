package com.example.demo.api.builder;

import com.example.demo.domain.FavoriteRecord;
import org.springframework.test.util.ReflectionTestUtils;

public final class FavoriteRecordBuilder {
    private Long id;
    private String companyCode;
    private Integer count;

    private FavoriteRecordBuilder() {
    }

    public static FavoriteRecordBuilder favoriteRecord() {
        return new FavoriteRecordBuilder();
    }

    public FavoriteRecordBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public FavoriteRecordBuilder companyCode(String companyCode) {
        this.companyCode = companyCode;
        return this;
    }

    public FavoriteRecordBuilder count(Integer count) {
        this.count = count;
        return this;
    }

    public FavoriteRecordBuilder but() {
        return favoriteRecord().id(id).companyCode(companyCode).count(count);
    }

    public FavoriteRecord build() {
        FavoriteRecord favoriteRecord = new FavoriteRecord();
        ReflectionTestUtils.setField(favoriteRecord, "id", this.id);
        ReflectionTestUtils.setField(favoriteRecord, "companyCode", this.companyCode);
        ReflectionTestUtils.setField(favoriteRecord, "count", this.count);

        return favoriteRecord;
    }
}
