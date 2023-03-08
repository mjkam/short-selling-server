package com.example.demo.cron;

import com.example.demo.AbstractIntegrationTest;
import com.example.demo.MockDataFetchProperties;
import com.example.demo.MockStockRecordApplier;
import com.example.demo.MockTimeManager;
import com.example.demo.api.builder.FetchRecordBuilder;
import com.example.demo.repository.FetchRecordRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;

import static com.example.demo.TimeUtils.localDate;
import static org.assertj.core.api.Assertions.assertThat;

public class DataFetchCronJobTests extends AbstractIntegrationTest {
    private DataFetchCronJob sut;

    @Autowired
    private FetchRecordRepository fetchRecordRepository;

    @BeforeEach
    void setup() {
        fetchRecordRepository.deleteAll();
    }

    @AfterEach
    void teardown() {
        fetchRecordRepository.deleteAll();
    }

    private void saveFetchRecord(LocalDate localDate) {
        fetchRecordRepository.save(FetchRecordBuilder.fetchRecord()
                .stockRecordDate(localDate)
                .build());
    }

    @Test
    @DisplayName("마지막 수집 날짜 이후부터 현재까지 데이터 fetch")
    void fetchDataBetweenLastAndNow() {
        //given
        LocalDate givenCurrentDate = localDate("2022-10-13");
        LocalDate lastFetchRecordSavedDate = localDate("2022-10-10");

        saveFetchRecord(lastFetchRecordSavedDate);
        MockStockRecordApplier mockStockRecordSaver = new MockStockRecordApplier();

        sut = new DataFetchCronJob(
                mockStockRecordSaver,
                fetchRecordRepository,
                new MockTimeManager(givenCurrentDate),
                new MockDataFetchProperties()
        );
        
        //when
        sut.fetch();

        //then
        List<LocalDate> shouldBeRequestedDates =
                List.of(localDate("2022-10-11"), localDate("2022-10-12"), localDate("2022-10-13"));
        assertSaveRequestedDates(mockStockRecordSaver.getRequestDates(), shouldBeRequestedDates);
    }

    @Test
    @DisplayName("fetch 가 처음이면, 지정된 시작 날짜부터 현재날짜까지 fetch")
    void whenNoFetchRecord_thenFetchStartFromConfiguredDate() {
        //given
        LocalDate givenCurrentDate = localDate("2022-06-10");
        LocalDate configuredStartDate = localDate("2022-06-08");

        MockStockRecordApplier mockStockRecordSaver = new MockStockRecordApplier();

        sut = new DataFetchCronJob(
                mockStockRecordSaver,
                fetchRecordRepository,
                new MockTimeManager(givenCurrentDate),
                new MockDataFetchProperties(configuredStartDate)
        );

        //when
        sut.fetch();

        //then
        List<LocalDate> shouldBeRequestedDates =
                List.of(localDate("2022-06-08"), localDate("2022-06-09"), localDate("2022-06-10"));
        assertSaveRequestedDates(mockStockRecordSaver.getRequestDates(), shouldBeRequestedDates);
    }

    private void assertSaveRequestedDates(List<LocalDate> localDates, List<LocalDate> containedDates) {
        boolean isSame = IntStream.range(0, localDates.size())
                .allMatch(idx -> localDates.get(idx).equals(containedDates.get(idx)));
        assertThat(isSame).isTrue();
    }
}
