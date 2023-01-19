package com.example.demo.cron;

import com.example.demo.TimeUtils;
import com.example.demo.config.RestTemplateConfiguration;
import com.example.demo.domain.MarketType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class KRXExternalApiTests {
    private final RestTemplateConfiguration restTemplateConfiguration = new RestTemplateConfiguration();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private RestTemplate restTemplate;

    @BeforeEach
    void setup() {
        restTemplate = restTemplateConfiguration.restTemplate(new RestTemplateBuilder());
    }

    @Test
    void getStockRecordsAtSpecificDate() {
        //given
        KRXApi krxApi = new KRXApi(restTemplate, objectMapper);

        //when
        List<KRXStockRecord> kospiResult = krxApi.getStockRecordsAt(TimeUtils.localDate("2022-10-12"), MarketType.KOSPI);
        List<KRXStockRecord> kosdaqResult = krxApi.getStockRecordsAt(TimeUtils.localDate("2022-10-12"), MarketType.KOSDAQ);

        //then
        assertThat(kospiResult.size()).isGreaterThan(0);
        assertThat(kosdaqResult.size()).isGreaterThan(0);
    }
}