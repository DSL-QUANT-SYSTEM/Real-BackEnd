package com.example.BeFETest.DTO.Bitcoin;

import com.example.BeFETest.Entity.Bitcoin.BitcoinResponse;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@Data
public class BitcoinDTO {
    @DateTimeFormat(pattern = "yyyy- MM- dd")
    private LocalDate date;
    private Double closingPrice;
    private Double openingPrice;
    private Double highPrice;
    private Double lowPrice;
    private String tradingVolume;
    private String fluctuatingRate;

    public BitcoinDTO(){}

    public BitcoinDTO(LocalDate date, Double closingPrice, String fluctuatingRate,
                      Double openingPrice, Double highPrice, Double lowPrice, String tradingVolume) {
        this.date = date;
        this.closingPrice = closingPrice;
        this.fluctuatingRate = fluctuatingRate;
        this.openingPrice = openingPrice;
        this.highPrice = highPrice;
        this.lowPrice = lowPrice;
        this.tradingVolume = tradingVolume;
    }

    public BitcoinResponse toEntity(){
        return BitcoinResponse.builder()
                .date(this.date)
                .closingPrice(this.closingPrice)
                .openingPrice(this.openingPrice)
                .highPrice(this.highPrice)
                .lowPrice(this.lowPrice)
                .tradingVolume(this.tradingVolume)
                .fluctuatingRate(this.fluctuatingRate)
                .build();
    }
}
