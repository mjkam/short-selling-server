package com.example.demo.scheduler;

import com.example.demo.TimeUtils;
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
        LocalDate localDate = TimeUtils.localDate("2022-10-12");
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("bld", "dbms/MDC/STAT/srt/MDCSTAT30501");
        body.add("trdDd", localDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        body.add("mktTpCd", "1");

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                "https://data.krx.co.kr/comm/bldAttendant/getJsonData.cmd",
                httpEntity,
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseDto responseDto = objectMapper.readValue(response.getBody(), ResponseDto.class);
        assertThat(responseDto.getRecords().size()).isGreaterThan(0);
    }
    


    static class ApiStockRecord {
        @JsonProperty("ISU_CD")
        private String companyCode;
        @JsonProperty("ISU_ABBRV")
        private String companyName;
        @JsonProperty("BAL_QTY")
        private String shortSellingShareCount;
        @JsonProperty("LIST_SHRS")
        private String listedShareCount;
        @JsonProperty("BAL_AMT")
        private String shortSellingAmount;
        @JsonProperty("MKTCAP")
        private String listedShareAmount;
        @JsonProperty("BAL_RTO")
        private String shortSellingShareRatio;
    }


    static class ResponseDto {
        @JsonProperty("OutBlock_1")
        private List<ApiStockRecord> records;
        @JsonProperty("CURRENT_DATETIME")
        private String serverDateTimeStr;

        public List<ApiStockRecord> getRecords() {
            return this.records;
        }
    }
}
