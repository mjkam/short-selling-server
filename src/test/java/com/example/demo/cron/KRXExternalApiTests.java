package com.example.demo.cron;

import com.example.demo.TimeUtils;
import com.example.demo.domain.MarketType;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class KRXExternalApiTests {

    @Test
    void getStockRecordsAtSpecificDate() throws JsonProcessingException {
        //given
        KRXApi krxApi = new KRXApi(new RestTemplate());
        System.setProperty("https.protocols", "TLSv1.2,TLSv1.1,TLSv1");

        //when
        List<KRXStockRecord> kospiResult = krxApi.getStockRecordsAt(TimeUtils.localDate("2022-10-12"), MarketType.KOSPI);
        List<KRXStockRecord> kosdaqResult = krxApi.getStockRecordsAt(TimeUtils.localDate("2022-10-12"), MarketType.KOSDAQ);

        //then
        assertThat(kospiResult.size()).isGreaterThan(0);
        assertThat(kosdaqResult.size()).isGreaterThan(0);
    }
}