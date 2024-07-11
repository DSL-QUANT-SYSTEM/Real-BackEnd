package com.example.BeFETest.DTO.kospi;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class KospiResponseDTO {

    private Long id;
    private String date;
    private double currentPrice;
    private double allDayRatio;
    private double percentChange;
    private List<KospiDTO> kospiData;

    public KospiResponseDTO() {}

    public KospiResponseDTO(Long id, String date, double currentPrice, double allDayRatio, double percentChange, List<KospiDTO> kospiData) {
        this.id = id;
        this.date = date;
        this.currentPrice = currentPrice;
        this.allDayRatio = allDayRatio;
        this.percentChange = percentChange;
        this.kospiData = kospiData;
    }
}
