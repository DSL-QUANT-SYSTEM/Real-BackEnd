package com.example.BeFETest.DTO.kospi200;

import com.example.BeFETest.Entity.kospi.KospiResponse;
import com.example.BeFETest.Entity.kospi200.Kospi200Response;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Kospi200ResponseDTO {
    private String date;
    private String closingPrice;
    private String openingPrice;
    private String highPrice;
    private String lowPrice;
    private String tradingVolume;
    private String fluctuatingRate;




    public Kospi200ResponseDTO() {}

    public Kospi200ResponseDTO(String date, String closingPrice, String fluctuatingRate,
                               String openingPrice, String highPrice, String lowPrice, String tradingVolume) {
        this.date = date;
        this.closingPrice = closingPrice;
        this.openingPrice = openingPrice;
        this.highPrice = highPrice;
        this.lowPrice = lowPrice;
        this.tradingVolume = tradingVolume;
        this.fluctuatingRate = fluctuatingRate;
    }
    public Kospi200Response toEntity(){
        return Kospi200Response.builder()
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
