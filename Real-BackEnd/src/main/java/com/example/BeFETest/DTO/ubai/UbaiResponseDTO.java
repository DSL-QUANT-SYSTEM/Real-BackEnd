package com.example.BeFETest.DTO.ubai;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UbaiResponseDTO {

    private Long id;
    private String date;
    private double currentPrice;
    private double allDayRatio;
    private double percentChange;
    private List<UbaiDTO> ubaiData;

    public UbaiResponseDTO() {}

    public UbaiResponseDTO(Long id, String date, double currentPrice, double allDayRatio, double percentChange, List<UbaiDTO> ubaiData) {
        this.id = id;
        this.date = date;
        this.currentPrice = currentPrice;
        this.allDayRatio = allDayRatio;
        this.percentChange = percentChange;
        this.ubaiData = ubaiData;
    }
}
