package com.example.demo.api;

import com.example.demo.api.builder.StockRecordBuilder;
import com.example.demo.controller.dto.GetTop50Response;
import com.example.demo.domain.StockRecord;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
public class ApiIntegrationTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void requestTop50Test() throws Exception {
        //given
        String requestDate = "2022-10-10";
        int top50ListSize = 50;


        //when
        MockHttpServletResponse response = mockMvc.perform(get("/top50")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        GetTop50Response getTop50Response = objectMapper.readValue(response.getContentAsString(), GetTop50Response.class);
        assertThat(getTop50Response.getRecords().size()).isEqualTo(top50ListSize);
        assertThat(getTop50Response.getRecordDate()).isEqualTo(requestDate);

    }

}
