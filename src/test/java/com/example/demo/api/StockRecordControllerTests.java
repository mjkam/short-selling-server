package com.example.demo.api;

import com.example.demo.BaseControllerTest;
import com.example.demo.api.builder.CompanyBuilder;
import com.example.demo.api.builder.StockRecordBuilder;
import com.example.demo.controller.StockRecordController;
import com.example.demo.controller.dto.GetStockRecordsResponse;
import com.example.demo.controller.dto.StockRecordDto;
import com.example.demo.domain.Company;
import com.example.demo.domain.MarketType;
import com.example.demo.domain.StockRecord;
import com.example.demo.exception.ExceptionCode;
import com.example.demo.exception.ExceptionResponse;
import com.example.demo.service.StockRecordService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static com.example.demo.TimeUtils.localDate;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StockRecordController.class)
public class StockRecordControllerTests extends BaseControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private StockRecordService stockRecordService;

    private CompanyBuilder companyBuilder;
    private StockRecordBuilder stockRecordBuilder;

    @BeforeEach
    void setup() {
        companyBuilder = CompanyBuilder.company()
                .name("")
                .logoImageName("")
                .marketType(MarketType.KOSPI);
        stockRecordBuilder = StockRecordBuilder.stockRecord()
                .listedShareCount(0L)
                .shortSellingAmount(0L)
                .shortSellingShareCount(0L)
                .listedShareAmount(0L)
                .shortSellingRatio(0f);
    }

    @ParameterizedTest
    @DisplayName("request 의 companyCode 가 유효하지 않은 길이면 BadRequest 리턴")
    @MethodSource("invalidCompanyCodes")
    void throwExceptionWhenRequestCompanyCodeExceedMaxLength(String longCompanyCode) throws Exception {
        //given

        //when
        MockHttpServletResponse response = mockMvc
                .perform(get(String.format("/company/%s/stock-records?duration=%d", longCompanyCode, 3)))
                .andReturn().getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());

        ExceptionResponse exceptionResponse = objectMapper.readValue(response.getContentAsString(), ExceptionResponse.class);
        assertThat(exceptionResponse.getCode()).isEqualTo(ExceptionCode.BAD_REQUEST.getCode());
    }

    private static Stream<String> invalidCompanyCodes() {
        return Stream.of(null, "123456789AA", "111111111111", "aa", "12345");
    }

    @ParameterizedTest
    @DisplayName("request 의 duration 이 유효하지 않은 범위이면 BadRequest 리턴")
    @ValueSource(ints = {131, 150})
    void throwExceptionWhenRequestCompanyCodeExceedMaxLength(int bigDuration) throws Exception {
        //given

        //when
        MockHttpServletResponse response = mockMvc
                .perform(get(String.format("/company/%s/stock-records?duration=%d", "000000", bigDuration)))
                .andReturn().getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());

        ExceptionResponse exceptionResponse = objectMapper.readValue(response.getContentAsString(), ExceptionResponse.class);
        assertThat(exceptionResponse.getCode()).isEqualTo(ExceptionCode.BAD_REQUEST.getCode());
    }

    @Test
    void getStockRecords() throws Exception {
        //given
        String givenCompanyCode = "000000";
        Company company = company(givenCompanyCode);
        StockRecord stockRecord1 = stockRecord(company, localDate("2022-10-13"));
        StockRecord stockRecord2 = stockRecord(company, localDate("2022-10-12"));
        StockRecord stockRecord3 = stockRecord(company, localDate("2022-10-11"));
        StockRecord stockRecord4 = stockRecord(company, localDate("2022-10-10"));
        given(stockRecordService.getStockRecords(givenCompanyCode, 3))
                .willReturn(List.of(stockRecord1, stockRecord2, stockRecord3, stockRecord4));

        //when
        MockHttpServletResponse response = mockMvc
                .perform(get(String.format("/company/%s/stock-records?duration=%d", givenCompanyCode, 3)))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        //then
        List<StockRecordDto> stockRecords = objectMapper.readValue(response.getContentAsString(), GetStockRecordsResponse.class).getStockRecords();

        assertThat(stockRecords.size()).isEqualTo(4);
        assertThat(stockRecords.get(0).getRecordDate()).isEqualTo(localDate("2022-10-13").toString());
        assertThat(stockRecords.get(1).getRecordDate()).isEqualTo(localDate("2022-10-12").toString());
        assertThat(stockRecords.get(2).getRecordDate()).isEqualTo(localDate("2022-10-11").toString());
        assertThat(stockRecords.get(3).getRecordDate()).isEqualTo(localDate("2022-10-10").toString());
    }

    private Company company(String companyCode) {
        return companyBuilder.but()
                .companyCode(companyCode)
                .build();
    }

    private StockRecord stockRecord(Company company, LocalDate localDate) {
        return stockRecordBuilder.but()
                .company(company)
                .recordDate(localDate)
                .build();
    }

}
