package com.example.BeFETest.DTO.kosdak;

import com.example.BeFETest.Entity.kosdak.KosdakResponse;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@Data
public class KosdakResponseDTO {
    private String date;
    private String closingPrice;
    private String openingPrice;
    private String highPrice;
    private String lowPrice;
    private String tradingVolume;
    private String fluctuatingRate;


    public KosdakResponseDTO() {}

    public KosdakResponseDTO(String date, String closingPrice, String fluctuatingRate,
                             String openingPrice, String highPrice, String lowPrice, String tradingVolume) {
        this.date = date;
        this.closingPrice = closingPrice;
        this.fluctuatingRate = fluctuatingRate;
        this.openingPrice = openingPrice;
        this.highPrice = highPrice;
        this.lowPrice = lowPrice;
        this.tradingVolume = tradingVolume;
    }

    public KosdakResponse toEntity(){
        return KosdakResponse.builder()
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
