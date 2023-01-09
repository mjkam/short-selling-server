package com.example.demo.api;

import com.example.demo.domain.FetchRecord;
import com.example.demo.repository.FetchRecordRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;

import java.util.List;

@DataJpaTest
public class Top50QueryTests {
    @Autowired
    private FetchRecordRepository fetchRecordRepository;

    @Test
    void getLatestFetchRecordTest() {
        //given

        //when
        List<FetchRecord> latestOne = fetchRecordRepository.findLatestOne(PageRequest.of(0, 1));

        //then
        Assertions.assertThat(latestOne.size()).isEqualTo(1);
    }
}
