package com.example.demo.api;

import com.example.demo.api.builder.CompanyBuilder;
import com.example.demo.api.builder.FavoriteRecordBuilder;
import com.example.demo.domain.Company;
import com.example.demo.domain.FavoriteRecord;
import com.example.demo.domain.MarketType;
import com.example.demo.repository.CompanyRepository;
import com.example.demo.repository.FavoriteRecordRepository;
import com.example.demo.service.FavoriteService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class FavoriteServiceTests {
    @Autowired
    private FavoriteService favoriteService;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private FavoriteRecordRepository favoriteRecordRepository;

    private CompanyBuilder companyBuilder;

    @BeforeEach
    void setup() {
        companyRepository.deleteAll();
        favoriteRecordRepository.deleteAll();

        companyBuilder = CompanyBuilder.company()
                .name("")
                .logoImageName("")
                .marketType(MarketType.KOSPI);
    }

    @AfterEach
    void teardown() {
        companyRepository.deleteAll();
        favoriteRecordRepository.deleteAll();
    }

    @Test
    @DisplayName("등록되지 않은 companyCode 를 favorite 요청하면 IllegalArgumentException 발생")
    void throwIllegalArgumentException_whenNotRegisteredCompanyCodeGiven() {
        //given
        String notRegisteredCompanyCode = "AAAAAA";

        //when then
        assertThatThrownBy(() -> favoriteService.registerFavorite(notRegisteredCompanyCode))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("favorite 에 company 가 처음 등록되면 favorite count 는 1")
    void favoriteCountShouldBe1_whenCompanyFirstRegisteredToFavorite() {
        //given
        String companyCode = "110011";
        companyRepository.save(company(companyCode));

        //when
        favoriteService.registerFavorite(companyCode);

        //then
        FavoriteRecord result = favoriteRecordRepository.findByCompanyCode(companyCode).orElse(null);
        assertThat(result).isNotNull();
        assertThat(result.getCount()).isEqualTo(1);
    }

    private Company company(String companyCode) {
        return companyBuilder.but()
                .companyCode(companyCode)
                .build();
    }

    private FavoriteRecord favoriteRecord(String companyCode, int count) {
        return FavoriteRecordBuilder.favoriteRecord()
                .companyCode(companyCode)
                .count(count)
                .build();
    }

    @Test
    @DisplayName("favorite 요청이 오면 favorite count 가 1씩 증가")
    void favoriteShouldBeIncrement_whenFavorited() {
        //given
        String companyCode = "110011";
        companyRepository.save(company(companyCode));
        favoriteRecordRepository.save(favoriteRecord(companyCode, 1));

        //when
        favoriteService.registerFavorite(companyCode);

        //then
        FavoriteRecord result = favoriteRecordRepository.findByCompanyCode(companyCode).orElse(null);
        assertThat(result).isNotNull();
        assertThat(result.getCount()).isEqualTo(2);
    }

}
