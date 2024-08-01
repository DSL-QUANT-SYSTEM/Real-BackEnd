package com.example.BeFETest.DTO.kospi;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class KospiResponseDTO {

    private Long id;
    private String date;
    private double closingPrice;
    private double comparison;
    private double fluctuationRate;
    private double openingPrice;
    private double highPrice;
    private double lowPrice;
    private double tradingVolume;
    private double tradingAmount;
    private double listedCapitalization;

    public KospiResponseDTO() {}

    public KospiResponseDTO(Long id, String date, double closingPrice, double comparison, double fluctuationRate,
                               double openingPrice, double highPrice, double lowPrice, double tradingVolume,
                               double tradingAmount, double listedCapitalization) {
        this.id = id;
        this.date = date;
        this.closingPrice = closingPrice;
        this.comparison = comparison;
        this.fluctuationRate = fluctuationRate;
        this.openingPrice = openingPrice;
        this.highPrice = highPrice;
        this.lowPrice = lowPrice;
        this.tradingVolume = tradingVolume;
        this.tradingAmount = tradingAmount;
        this.listedCapitalization = listedCapitalization;
    }
}
