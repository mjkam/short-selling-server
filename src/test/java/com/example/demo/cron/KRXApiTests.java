package com.example.demo.cron;

import com.example.demo.BaseTest;
import com.example.demo.config.RestTemplateConfiguration;
import com.example.demo.domain.MarketType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static com.example.demo.TimeUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

public class KRXApiTests extends BaseTest {
    private final RestTemplateConfiguration restTemplateConfiguration = new RestTemplateConfiguration();

    private RestTemplate restTemplate;

    @BeforeEach
    void setup() {
        restTemplate = restTemplateConfiguration.restTemplate(new RestTemplateBuilder());
    }

    @Test
    @DisplayName("KRX StockRecord 요청")
    void requestStockRecordsToKRX() {
        //given
        KRXApi krxApi = new KRXApi(restTemplate, objectMapper);

        //when
        List<KRXStockRecord> kospiResult = krxApi.getStockRecordsAt(localDate("2022-10-12"), MarketType.KOSPI);
        List<KRXStockRecord> kosdaqResult = krxApi.getStockRecordsAt(localDate("2022-10-12"), MarketType.KOSDAQ);

        //then
        assertThat(kospiResult.size()).isGreaterThan(0);
        assertThat(kosdaqResult.size()).isGreaterThan(0);
    }
}