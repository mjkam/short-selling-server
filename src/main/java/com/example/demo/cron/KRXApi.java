package com.example.demo.cron;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class KRXApi {
    public List<KRXStockRecord> getStockRecordsAt(LocalDate localDate) throws JsonProcessingException {
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
        ObjectMapper objectMapper = new ObjectMapper();
        GetKRXStockRecordsResponse getKRXStockRecordsResponse = objectMapper.readValue(response.getBody(), GetKRXStockRecordsResponse.class);
        return getKRXStockRecordsResponse.getRecords();
    }
}
