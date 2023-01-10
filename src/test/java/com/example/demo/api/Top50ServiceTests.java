package com.example.demo.api;

import com.example.demo.domain.FetchRecord;
import com.example.demo.domain.StockRecord;
import com.example.demo.repository.FetchRecordRepository;
import com.example.demo.repository.StockRecordRepository;
import com.example.demo.service.Top50Service;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class Top50ServiceTests {
    @Mock
    private FetchRecordRepository fetchRecordRepository;
    @Mock
    private StockRecordRepository stockRecordRepository;


    private FetchRecord fetchRecord(LocalDate stockRecordDate, LocalDateTime executionDateTime) {
        FetchRecord fetchRecord = new FetchRecord();
        ReflectionTestUtils.setField(fetchRecord, "stockRecordDate", stockRecordDate);
        ReflectionTestUtils.setField(fetchRecord, "executedDateTime", executionDateTime);

        return fetchRecord;
    }

    private List<StockRecord> stockRecords(LocalDate recordDate, int count) {
        List<StockRecord> stockRecords = new ArrayList<>();
        for (int i=0; i<count; i++) {
            StockRecord stockRecord = new StockRecord();
            ReflectionTestUtils.setField(stockRecord, "recordDate", recordDate);
            stockRecords.add(stockRecord);
        }
        return stockRecords;
    }

    private LocalDate localDate(String s) {
        return LocalDate.parse(s, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    private LocalDateTime localDateTime(String s) {
        return LocalDateTime.parse(s, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    @Test
    void getTop50Test() {
        //given
        int top50RecordNum = 50;
        LocalDate stockRecordDate = localDate("2022-10-10");
        LocalDateTime executionDateTime = localDateTime("2022-12-12 00:00:00");
        FetchRecord fetchRecord = fetchRecord(stockRecordDate, executionDateTime);

        given(fetchRecordRepository.findByOrderByStockRecordDateDesc(PageRequest.of(0, 1))).willReturn(List.of(fetchRecord));
        given(stockRecordRepository.findByRecordDateOrderByShortSellingRatioDesc(stockRecordDate, PageRequest.of(0, top50RecordNum))).willReturn(stockRecords(stockRecordDate, top50RecordNum));
        Top50Service top50Service = new Top50Service(fetchRecordRepository, stockRecordRepository);

        //when
        List<StockRecord> top50Records = top50Service.getTop50();

        //then
        assertThat(top50Records.size()).isEqualTo(top50RecordNum);
        assertThat(top50Records.get(0).getRecordDate()).isEqualTo(stockRecordDate);
    }

    @Test
    void throwExceptionWhenFetchRecordNotExist() {
        //given
        given(fetchRecordRepository.findByOrderByStockRecordDateDesc(PageRequest.of(0, 1))).willReturn(new ArrayList<>());
        Top50Service top50Service = new Top50Service(fetchRecordRepository, stockRecordRepository);

        //when
        assertThatThrownBy(top50Service::getTop50)
                .isInstanceOf(EntityNotFoundException.class);

        //then
    }

    @Test
    void throwExceptionWhenStockRecordListSizeIsNot50() {
        //given
        int top50RecordNum = 50;
        LocalDate stockRecordDate = localDate("2022-10-10");
        LocalDateTime executionDateTime = localDateTime("2022-12-12 00:00:00");
        FetchRecord fetchRecord = fetchRecord(stockRecordDate, executionDateTime);

        given(fetchRecordRepository.findByOrderByStockRecordDateDesc(PageRequest.of(0, 1))).willReturn(List.of(fetchRecord));
        given(stockRecordRepository.findByRecordDateOrderByShortSellingRatioDesc(stockRecordDate, PageRequest.of(0, top50RecordNum))).willReturn(stockRecords(stockRecordDate, 1));
        Top50Service top50Service = new Top50Service(fetchRecordRepository, stockRecordRepository);

        //when
        assertThatThrownBy(top50Service::getTop50)
                .isInstanceOf(IllegalStateException.class);

        //then

    }

}
