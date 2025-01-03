package com.example.BeFETest.DTO.kospi200;

import com.example.BeFETest.Entity.kospi.KospiResponse;
import com.example.BeFETest.Entity.kospi200.Kospi200Response;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Data
public class Kospi200ResponseDTO {
    private LocalDate date;
    private Double closingPrice;
    private Double openingPrice;
    private Double highPrice;
    private Double lowPrice;
    private String tradingVolume;
    private String fluctuatingRate;




    public Kospi200ResponseDTO() {}

    public Kospi200ResponseDTO(LocalDate date, Double closingPrice, String fluctuatingRate,
                               Double openingPrice, Double highPrice, Double lowPrice, String tradingVolume) {
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
