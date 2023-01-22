package com.example.demo.api;

import com.example.demo.BaseControllerTest;
import com.example.demo.controller.FavoriteController;
import com.example.demo.controller.dto.RegisterFavoriteRequest;
import com.example.demo.exception.ExceptionCode;
import com.example.demo.exception.ExceptionResponse;
import com.example.demo.service.FavoriteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@WebMvcTest(FavoriteController.class)
public class FavoriteControllerTests extends BaseControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FavoriteService favoriteService;

    private static Stream<String> invalidCompanyCodes() {
        return Stream.of(null, "", "123456789AA", "111111111111", "aa", "12345");
    }


    @ParameterizedTest
    @DisplayName("request 의 companyCode 가 유효하지 않은 길이면 BadRequest 발생")
    @MethodSource("invalidCompanyCodes")
    void throwExceptionWhenRequestCompanyCodeExceedMaxLength(String longCompanyCode) throws Exception {
        //given
        RegisterFavoriteRequest request = registerFavoriteRequest(longCompanyCode);

        //when
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.post("/favorite")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn().getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());

        ExceptionResponse exceptionResponse = objectMapper.readValue(response.getContentAsString(), ExceptionResponse.class);
        assertThat(exceptionResponse.getCode()).isEqualTo(ExceptionCode.BAD_REQUEST.getCode());
    }

    @Test
    @DisplayName("유효하지 않은 request body 이면 BadRequest 발생")
    void throwBadRequest_WhenRequestBodyIsInvalid() throws Exception {
        //given
        String body = "{INVALID_BODY}";

        //when
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.post("/favorite")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andReturn().getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());

        ExceptionResponse exceptionResponse = objectMapper.readValue(response.getContentAsString(), ExceptionResponse.class);
        assertThat(exceptionResponse.getCode()).isEqualTo(ExceptionCode.BAD_REQUEST.getCode());
    }


    @Test
    @DisplayName("favorite 등록")
    void registerFavoriteCompany() throws Exception {
        //given
        RegisterFavoriteRequest request = registerFavoriteRequest("000000");

        //when then
        mockMvc.perform(MockMvcRequestBuilders.post("/favorite")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    private RegisterFavoriteRequest registerFavoriteRequest(String companyCode) {
        RegisterFavoriteRequest request = new RegisterFavoriteRequest();
        ReflectionTestUtils.setField(request, "companyCode", companyCode);

        return request;
    }
}
