package com.example.demo.api;

import com.example.demo.BaseControllerTest;
import com.example.demo.TimeUtils;
import com.example.demo.api.builder.CompanyBuilder;
import com.example.demo.api.builder.StockRecordBuilder;
import com.example.demo.controller.Top50Controller;
import com.example.demo.controller.dto.GetTop50Response;
import com.example.demo.domain.Company;
import com.example.demo.domain.MarketType;
import com.example.demo.domain.StockRecord;
import com.example.demo.service.Top50Service;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

@WebMvcTest(Top50Controller.class)
public class Top50ControllerTests extends BaseControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private Top50Service top50Service;

    private StockRecordBuilder stockRecordBuilder;
    private CompanyBuilder companyBuilder;

    @BeforeEach
    void setup() {
        stockRecordBuilder = StockRecordBuilder.stockRecord()
                .recordDate(TimeUtils.localDate("2022-10-10"))
                .listedShareCount(0L)
                .shortSellingAmount(0L)
                .shortSellingShareCount(0L)
                .listedShareAmount(0L)
                .shortSellingRatio(0f);
        companyBuilder = CompanyBuilder.company()
                .name("")
                .logoImageName("")
                .marketType(MarketType.KOSPI);
    }

    private Company company(String companyCode) {
        return companyBuilder.but()
                .companyCode(companyCode)
                .build();
    }

    private StockRecord stockRecord(Company company) {
        return stockRecordBuilder.but()
                .company(company)
                .build();
    }

    @Test
    @DisplayName("top50 요청 테스트")
    void getTop50StockRecords() throws Exception {
        //given
        Company company1 = company("000001");
        Company company2 = company("000002");

        StockRecord stockRecord1 = stockRecord(company1);
        StockRecord stockRecord2 = stockRecord(company2);

        BDDMockito.given(top50Service.getTop50())
                .willReturn(List.of(stockRecord1, stockRecord2));

        //when
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/top50"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse();

        //then
        List<GetTop50Response.Top50ListItem> top50Records = objectMapper.readValue(response.getContentAsString(), GetTop50Response.class).getTop50Records();
        Assertions.assertThat(top50Records.get(0).getCompanyCode()).isEqualTo("000001");
        Assertions.assertThat(top50Records.get(1).getCompanyCode()).isEqualTo("000002");
    }

}
