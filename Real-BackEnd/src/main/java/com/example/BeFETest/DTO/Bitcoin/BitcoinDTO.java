package com.example.BeFETest.DTO.Bitcoin;

import com.example.BeFETest.Entity.Bitcoin.BitcoinResponse;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Data
public class BitcoinDTO {
    private String date;
    private String closingPrice;
    private String openingPrice;
    private String highPrice;
    private String lowPrice;
    private String tradingVolume;
    private String fluctuatingRate;

    public BitcoinDTO(){}

    public BitcoinDTO(String date, String closingPrice, String fluctuatingRate,
                             String openingPrice, String highPrice, String lowPrice, String tradingVolume) {
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
