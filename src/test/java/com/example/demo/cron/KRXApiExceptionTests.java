package com.example.demo.cron;

import com.example.demo.TimeUtils;
import com.example.demo.config.RestTemplateConfiguration;
import com.example.demo.domain.MarketType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;

@RestClientTest(value = {KRXApi.class, RestTemplateConfiguration.class})
public class KRXApiExceptionTests {
    @Autowired
    private KRXApi krxApi;

    @Autowired
    private MockRestServiceServer mockRestServiceServer;

    @Test
    @DisplayName("KRX API 에서 Json 이 아닌 값을 리턴하면 Exception 발생")
    void throwExceptionWhenKRXApiReturnNotJson() {
        //given
        mockRestServiceServer.expect(MockRestRequestMatchers.requestTo("https://data.krx.co.kr/comm/bldAttendant/getJsonData.cmd"))
                .andRespond(MockRestResponseCreators.withSuccess("<html></html>", MediaType.TEXT_HTML));

        //when then
        Assertions.assertThatThrownBy(
                () -> krxApi.getStockRecordsAt(TimeUtils.localDate("2022-10-12"), MarketType.KOSPI))
                .isInstanceOf(KRXApiResponseException.class);
    }
}