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
    private Double closing_price;
    private Double opening_price;
    private Double high_price;
    private Double low_price;
    private String trading_volume;
    private String fluctuating_rate;
    private String market;

    public CoinDetailsDTO(){}

    public CoinDetailsDTO(LocalDate date, Double closingPrice, String fluctuatingRate,
                          Double openingPrice, Double highPrice, Double lowPrice, String tradingVolume, String market) {
        this.date = date;
        this.closing_price = closingPrice;
        this.opening_price = openingPrice;
        this.high_price = highPrice;
        this.low_price = lowPrice;
        this.trading_volume = tradingVolume;
        this.fluctuating_rate = fluctuatingRate;
        this.market = market;
    }


}