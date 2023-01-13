package com.example.demo.domain;

public enum MarketType {
    KOSPI(1),
    KOSDAQ(2),
    ;

    private final Integer marketNum;

    MarketType(int marketNum) {
        this.marketNum = marketNum;
    }

    public int getMarketNum() {
        return this.marketNum;
    }
}
