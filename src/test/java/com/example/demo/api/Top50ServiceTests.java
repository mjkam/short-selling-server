package com.example.demo.api;

import com.example.demo.api.builder.FetchRecordBuilder;
import com.example.demo.api.builder.StockRecordBuilder;
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
import java.util.ArrayList;
import java.util.List;

import static com.example.demo.TimeUtils.localDate;
import static com.example.demo.TimeUtils.localDateTime;
import static com.example.demo.api.builder.FetchRecordBuilder.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class Top50ServiceTests {
    @Mock
    private FetchRecordRepository fetchRecordRepository;
    @Mock
    private StockRecordRepository stockRecordRepository;

    @Test
    void getTop50Test() {
        //given
        FetchRecord fetchRecord = fetchRecord().stockRecordDate(localDate("2022-10-11")).build();
        List<StockRecord> stockRecords = createStockRecords(localDate("2022-10-11"), 50);

        given(fetchRecordRepository.findByOrderByStockRecordDateDesc(PageRequest.of(0, 1))).willReturn(List.of(fetchRecord));
        given(stockRecordRepository.findByRecordDateOrderByShortSellingRatioDesc(localDate("2022-10-11"), PageRequest.of(0, 50)))
                .willReturn(stockRecords);
        Top50Service top50Service = new Top50Service(fetchRecordRepository, stockRecordRepository);

        //when
        List<StockRecord> top50Records = top50Service.getTop50();

        //then
        assertThat(top50Records.size()).isEqualTo(50);
        assertThat(top50Records.get(0).getRecordDate()).isEqualTo(localDate("2022-10-11"));
    }

    private List<StockRecord> createStockRecords(LocalDate recordDate, int count) {
        List<StockRecord> result = new ArrayList<>();
        for (int i=0; i<count; i++) {
            result.add(StockRecordBuilder.stockRecord()
                    .recordDate(recordDate)
                    .shortSellingRatio((float) i).build());
        }
        return result;
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
        FetchRecord fetchRecord = fetchRecord().stockRecordDate(localDate("2022-10-11")).build();
        List<StockRecord> stockRecords = createStockRecords(localDate("2022-10-11"), 10);

        given(fetchRecordRepository.findByOrderByStockRecordDateDesc(PageRequest.of(0, 1))).willReturn(List.of(fetchRecord));
        given(stockRecordRepository.findByRecordDateOrderByShortSellingRatioDesc(localDate("2022-10-11"), PageRequest.of(0, 50)))
                .willReturn(stockRecords);
        Top50Service top50Service = new Top50Service(fetchRecordRepository, stockRecordRepository);

        //when
        assertThatThrownBy(top50Service::getTop50)
                .isInstanceOf(IllegalStateException.class);

        //then

    }

}
