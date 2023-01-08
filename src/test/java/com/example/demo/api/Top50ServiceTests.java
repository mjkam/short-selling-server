package com.example.demo.api;

import com.example.demo.domain.StockRecord;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class Top50ServiceTests {
    private Top50Service top50Service = new Top50Service();

    @Test
    void getTop50Test() {
        //given
        int top50RecordNum = 50;

        //when
        List<StockRecord> top50Records = top50Service.getTop50();

        //then
        assertThat(top50Records.size()).isEqualTo(top50RecordNum);
    }

    private static class Top50Service {

        public List<StockRecord> getTop50() {
            return new ArrayList<>();
        }
    }
}
