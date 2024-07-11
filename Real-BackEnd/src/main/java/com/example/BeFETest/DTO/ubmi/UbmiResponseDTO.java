package com.example.BeFETest.DTO.ubmi;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UbmiResponseDTO {

    private Long id;
    private String date;
    private double currentPrice;
    private double allDayRatio;
    private double percentChange;
    private List<UbmiDTO> ubmiData;

    public UbmiResponseDTO() {}

    public UbmiResponseDTO(Long id, String date, double currentPrice, double allDayRatio, double percentChange, List<UbmiDTO> ubmiData) {
        this.id = id;
        this.date = date;
        this.currentPrice = currentPrice;
        this.allDayRatio = allDayRatio;
        this.percentChange = percentChange;
        this.ubmiData = ubmiData;
    }
}
