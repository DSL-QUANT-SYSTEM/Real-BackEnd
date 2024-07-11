package com.example.BeFETest.DTO.kosdak;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class KosdakResponseDTO {

    private Long id;
    private String date;
    private double currentPrice;
    private double allDayRatio;
    private double percentChange;
    private List<KosdakDTO> kosdakData;

    public KosdakResponseDTO() {}

    public KosdakResponseDTO(Long id, String date, double currentPrice, double allDayRatio, double percentChange, List<KosdakDTO> kosdakData) {
        this.id = id;
        this.date = date;
        this.currentPrice = currentPrice;
        this.allDayRatio = allDayRatio;
        this.percentChange = percentChange;
        this.kosdakData = kosdakData;
    }
}
