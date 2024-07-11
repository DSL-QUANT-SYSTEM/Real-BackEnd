package com.example.BeFETest.DTO.ubmi30;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Ubmi30ResponseDTO {

    private Long id;
    private String date;
    private double currentPrice;
    private double allDayRatio;
    private double percentChange;
    private List<Ubmi30DTO> ubmi30Data;

    public Ubmi30ResponseDTO() {}

    public Ubmi30ResponseDTO(Long id, String date, double currentPrice, double allDayRatio, double percentChange, List<Ubmi30DTO> ubmi30Data) {
        this.id = id;
        this.date = date;
        this.currentPrice = currentPrice;
        this.allDayRatio = allDayRatio;
        this.percentChange = percentChange;
        this.ubmi30Data = ubmi30Data;
    }
}
