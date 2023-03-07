package com.example.demo.api;

import com.example.demo.AbstractIntegrationTest;
import com.example.demo.api.builder.FavoriteRecordBuilder;
import com.example.demo.controller.dto.RegisterFavoriteRequest;
import com.example.demo.domain.Company;
import com.example.demo.domain.FavoriteRecord;
import com.example.demo.repository.CompanyRepository;
import com.example.demo.repository.FavoriteRecordRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import static com.example.demo.api.builder.CompanyBuilder.company;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class FavoriteIntegrationTests extends AbstractIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private FavoriteRecordRepository favoriteRecordRepository;

    @BeforeEach
    void setup() {
        companyRepository.deleteAll();
        favoriteRecordRepository.deleteAll();
    }

    @AfterEach
    void teardown() {
        companyRepository.deleteAll();
        favoriteRecordRepository.deleteAll();
    }

    @Test
    @DisplayName("등록되지 않은 companyCode 를 favorite 요청하면 HttpStatus Bad Request 리턴")
    void returnBadRequest_whenNotRegisteredCompanyCodeRequested() throws Exception {
        //given
        String notRegisteredCompanyCode = "AAAAAA";

        //when then
        mockMvc.perform(post("/favorite")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerFavoriteRequest(notRegisteredCompanyCode))))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("favorite api 요청하면 favorite count 1씩 증가")
    void favoriteCountShouldBeIncrement_whenFavorited() throws Exception {
        //given
        String companyCode = "110011";

        companyRepository.save(company().companyCode(companyCode).build());
        favoriteRecordRepository.save(FavoriteRecordBuilder.favoriteRecord()
                .companyCode(companyCode)
                .count(1)
                .build());

        //when
        mockMvc.perform(post("/favorite")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerFavoriteRequest(companyCode))))
                .andExpect(status().isOk());

        //then
        FavoriteRecord resultFavoriteRecord = favoriteRecordRepository.findByCompanyCode(companyCode).orElse(null);
        assertThat(resultFavoriteRecord).isNotNull();
        assertThat(resultFavoriteRecord.getCompanyCode()).isEqualTo(companyCode);
        assertThat(resultFavoriteRecord.getCount()).isEqualTo(2);
    }


    @Test
    @DisplayName("favorite 에 company 가 처음 등록되면 favorite count 를 1로 저장")
    void favoriteCountShouldBe1_whenCompanyFirstRegisteredToFavorite() throws Exception {
        //given
        String companyCode = "110011";

        Company company = company().companyCode(companyCode).build();
        companyRepository.save(company);

        //when
        mockMvc.perform(post("/favorite")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerFavoriteRequest(companyCode))))
                .andExpect(status().isOk());

        //then
        FavoriteRecord resultFavoriteRecord = favoriteRecordRepository.findByCompanyCode(companyCode).orElse(null);
        assertThat(resultFavoriteRecord).isNotNull();
        assertThat(resultFavoriteRecord.getCompanyCode()).isEqualTo(companyCode);
        assertThat(resultFavoriteRecord.getCount()).isEqualTo(1);
    }

    private RegisterFavoriteRequest registerFavoriteRequest(String companyCode) {
        RegisterFavoriteRequest request = new RegisterFavoriteRequest();
        ReflectionTestUtils.setField(request, "companyCode", companyCode);

        return request;
    }

}
