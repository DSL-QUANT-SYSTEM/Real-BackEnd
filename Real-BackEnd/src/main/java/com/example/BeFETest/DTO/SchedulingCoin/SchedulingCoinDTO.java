package com.example.BeFETest.DTO.SchedulingCoin;

import com.example.BeFETest.Entity.Bitcoin.BitcoinResponse;
import com.example.BeFETest.Entity.SchedulingCoin.SchedulingCoinResponse;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Getter
@Setter
@Data
public class SchedulingCoinDTO {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private Double closingPrice;
    private Double openingPrice;
    private Double highPrice;
    private Double lowPrice;
    private String tradingVolume;
    private String fluctuatingRate;
    private String market;

    public SchedulingCoinDTO(){}

    public SchedulingCoinDTO(LocalDate date, Double closingPrice, String fluctuatingRate,
                      Double openingPrice, Double highPrice, Double lowPrice,
                             String market,String tradingVolume) {
        this.date = date;
        this.closingPrice = closingPrice;
        this.fluctuatingRate = fluctuatingRate;
        this.openingPrice = openingPrice;
        this.highPrice = highPrice;
        this.lowPrice = lowPrice;
        this.tradingVolume = tradingVolume;
        this.market = market;
    }

    public SchedulingCoinResponse toEntity(){
        return SchedulingCoinResponse.builder()
                .date(this.date)
                .closingPrice(this.closingPrice)
                .openingPrice(this.openingPrice)
                .highPrice(this.highPrice)
                .lowPrice(this.lowPrice)
                .tradingVolume(this.tradingVolume)
                .fluctuatingRate(this.fluctuatingRate)
                .market(this.market)
                .build();
    }

}

