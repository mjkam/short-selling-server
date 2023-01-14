package com.example.demo.scheduler;

import com.example.demo.TimeUtils;
import com.example.demo.cron.KRXApi;
import com.example.demo.cron.KRXStockRecord;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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