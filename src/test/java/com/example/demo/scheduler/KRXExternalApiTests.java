package com.example.demo.scheduler;

import com.example.demo.TimeUtils;
import com.example.demo.cron.KRXApi;
import com.example.demo.cron.KRXStockRecord;
import com.example.demo.domain.Company;
import com.example.demo.domain.StockRecord;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class KRXExternalApiTests {

    @Test
    void getStockRecordsAtSpecificDate() throws JsonProcessingException {
        //given
        KRXApi krxApi = new KRXApi();

        //when
        List<KRXStockRecord> result = krxApi.getStockRecordsAt(TimeUtils.localDate("2022-10-12"));

        //then
        assertThat(result.size()).isGreaterThan(0);
    }


}
