package com.example.demo.cron;

import com.example.demo.api.builder.KRXStockRecordBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class KRXStockRecordTests {

    @Test
    @DisplayName("KRXApi 에서 리턴한 문자열로 된 숫자를 long 으로 변환")
    void changeStringToLong() {
        KRXStockRecord krxStockRecord = KRXStockRecordBuilder.krxStockRecord()
                .shortSellingShareCount("1,111")
                .shortSellingAmount("22,222,222")
                .listedShareCount("333")
                .listedShareAmount("5,555")
                .shortSellingShareRatio("1.11")
                .build();

        assertThat(krxStockRecord.getShortSellingShareCount()).isEqualTo(1111L);
        assertThat(krxStockRecord.getShortSellingAmount()).isEqualTo(22222222L);
        assertThat(krxStockRecord.getListedShareCount()).isEqualTo(333L);
        assertThat(krxStockRecord.getListedShareAmount()).isEqualTo(5555L);
        assertThat(krxStockRecord.getShortSellingShareRatio()).isEqualTo(1.11f);
    }
}