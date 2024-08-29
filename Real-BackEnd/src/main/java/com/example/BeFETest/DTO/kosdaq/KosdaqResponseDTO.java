package com.example.BeFETest.DTO.kosdaq;

import com.example.BeFETest.Entity.kosdaq.KosdaqResponse;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@Data
public class KosdaqResponseDTO {
    @DateTimeFormat(pattern = "yyyy- MM- dd")
    private LocalDate date;

    private Double closingPrice;
    private Double openingPrice;
    private Double highPrice;
    private Double lowPrice;
    private String tradingVolume;
    private String fluctuatingRate;


    public KosdaqResponseDTO() {}

    public KosdaqResponseDTO(LocalDate date, Double closingPrice, String fluctuatingRate,
                             Double openingPrice, Double highPrice, Double lowPrice, String tradingVolume) {
        this.date = date;
        this.closingPrice = closingPrice;
        this.fluctuatingRate = fluctuatingRate;
        this.openingPrice = openingPrice;
        this.highPrice = highPrice;
        this.lowPrice = lowPrice;
        this.tradingVolume = tradingVolume;
    }

    public KosdaqResponse toEntity(){
        return KosdaqResponse.builder()
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
