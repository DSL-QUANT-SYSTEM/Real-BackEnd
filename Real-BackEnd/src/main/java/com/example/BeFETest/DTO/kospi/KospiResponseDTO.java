package com.example.BeFETest.DTO.kospi;

import com.example.BeFETest.Entity.kospi.KospiResponse;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@Data
public class KospiResponseDTO {
    @DateTimeFormat(pattern = "yyyy- MM- dd")
    private LocalDate date;
    private Double closingPrice;
    private Double openingPrice;
    private Double highPrice;
    private Double lowPrice;
    private String tradingVolume;
    private String fluctuatingRate;

    public KospiResponseDTO() {}

    public KospiResponseDTO(LocalDate date, Double closingPrice, String fluctuatingRate,
                            Double openingPrice, Double highPrice, Double lowPrice, String tradingVolume) {
        this.date = date;
        this.closingPrice = closingPrice;
        this.openingPrice = openingPrice;
        this.highPrice = highPrice;
        this.lowPrice = lowPrice;
        this.tradingVolume = tradingVolume;
        this.fluctuatingRate = fluctuatingRate;
    }

    public KospiResponse toEntity(){
        return KospiResponse.builder()
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
