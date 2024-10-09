package com.example.BeFETest.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@Data
public class CoinDetailsDTO {
    @DateTimeFormat(pattern = "yyyy- MM- dd")
    private LocalDate date;
    private Double closingPrice;
    private Double openingPrice;
    private Double highPrice;
    private Double lowPrice;
    private String tradingVolume;
    private String fluctuatingRate;
    private String market;

    public CoinDetailsDTO(){}

    public CoinDetailsDTO(LocalDate date, Double closingPrice, String fluctuatingRate,
                          Double openingPrice, Double highPrice, Double lowPrice, String tradingVolume, String market) {
        this.date = date;
        this.closingPrice = closingPrice;
        this.openingPrice = openingPrice;
        this.highPrice = highPrice;
        this.lowPrice = lowPrice;
        this.tradingVolume = tradingVolume;
        this.fluctuatingRate = fluctuatingRate;
        this.market = market;
    }


}