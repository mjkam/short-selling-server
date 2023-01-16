package com.example.demo.api;

import com.example.demo.controller.dto.RegisterFavoriteRequest;
import com.example.demo.domain.FavoriteRecord;
import com.example.demo.repository.FavoriteRecordRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class FavoriteIntegrationTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private FavoriteRecordRepository favoriteRecordRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void registerFavoriteRequest() throws Exception {
        //given
        RegisterFavoriteRequest request = registerFavoriteRequest("testCompanyCode");

        //when
        mockMvc.perform(MockMvcRequestBuilders.post("/favorite")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk());
        //then
        FavoriteRecord favoriteRecord = favoriteRecordRepository.findByCompanyCode("testCompanyCode").orElse(null);
        Assertions.assertThat(favoriteRecord).isNotNull();
        Assertions.assertThat(favoriteRecord.getCount()).isEqualTo(1);
    }

    private RegisterFavoriteRequest registerFavoriteRequest(String companyCode) {
        RegisterFavoriteRequest request = new RegisterFavoriteRequest();
        ReflectionTestUtils.setField(request, "companyCode", companyCode);

        return request;
    }
}
