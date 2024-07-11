package com.example.BeFETest.DTO.kosdak2000;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Kosdak2000ResponseDTO {

    private Long id;
    private String date;
    private double currentPrice;
    private double allDayRatio;
    private double percentChange;
    private List<Kosdak2000DTO> kosdak2000Data;

    public Kosdak2000ResponseDTO() {}

    public Kosdak2000ResponseDTO(Long id, String date, double currentPrice, double allDayRatio, double percentChange, List<Kosdak2000DTO> kosdak2000Data) {
        this.id = id;
        this.date = date;
        this.currentPrice = currentPrice;
        this.allDayRatio = allDayRatio;
        this.percentChange = percentChange;
        this.kosdak2000Data = kosdak2000Data;
    }
}
