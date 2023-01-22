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


//    @Test
//    @DisplayName("가져온 KRXStockRecord 를 StockRecord 로 저장")
//    void saveStockRecordsFromKRXStockRecords() {
//        //given
//        LocalDate givenDate = localDate("2022-10-12");
//
//        String companyCode1 = "000001";
//        KRXStockRecord krxStockRecord1 = KRXStockRecordBuilder.krxStockRecord().companyName("")
//                .companyCode(companyCode1)
//                .shortSellingShareCount("1,111")
//                .shortSellingAmount("1,111")
//                .listedShareCount("1,111")
//                .listedShareAmount("1,111")
//                .shortSellingShareRatio("1.11")
//                .build();
//        String companyCode2 = "000002";
//        KRXStockRecord krxStockRecord2 = KRXStockRecordBuilder.krxStockRecord().companyName("")
//                .companyCode(companyCode2)
//                .shortSellingShareCount("2,222")
//                .shortSellingAmount("2,222")
//                .listedShareCount("2,222")
//                .listedShareAmount("2,222")
//                .shortSellingShareRatio("2.22")
//                .build();
//
//        given(krxApi.getStockRecordsAt(givenDate, MarketType.KOSPI))
//                .willReturn(List.of(krxStockRecord1));
//        given(krxApi.getStockRecordsAt(givenDate, MarketType.KOSPI))
//                .willReturn(List.of(krxStockRecord2));
//
//        //when
//        sut.save(givenDate);
//
//        //then
//        List<FetchRecord> allFetchRecords = fetchRecordRepository.findAll();
//        assertThat(allFetchRecords.size()).isEqualTo(1);
//        assertThat(allFetchRecords.get(0).getStockRecordDate()).isEqualTo(givenDate);
//    }