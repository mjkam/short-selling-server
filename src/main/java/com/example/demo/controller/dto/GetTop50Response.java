package com.example.demo.controller.dto;

import com.example.demo.domain.StockRecord;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
public class GetTop50Response {
    private List<Top50ListItem> top50Records;
    private String recordDate;

    public GetTop50Response(List<StockRecord> top50Records) {
        this.top50Records = top50Records.stream()
                .map(Top50ListItem::new)
                .collect(Collectors.toList());
        this.recordDate = top50Records.get(0).getRecordDate().toString();
    }


    @NoArgsConstructor
    @Getter
    public static class Top50ListItem {
        private String companyCode;
        private String companyName;
        private String logoImageUrl;
        private Float shortSellingRatio;

        public Top50ListItem(StockRecord stockRecord) {
            this.companyCode = stockRecord.getCompany().getCompanyCode();
            this.companyName = stockRecord.getCompany().getName();
            this.logoImageUrl = stockRecord.getCompany().getLogoImageName();
            this.shortSellingRatio = stockRecord.getShortSellingRatio();
        }
    }
}