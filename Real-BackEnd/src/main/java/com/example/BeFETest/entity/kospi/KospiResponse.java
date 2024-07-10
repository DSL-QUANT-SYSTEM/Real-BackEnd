package com.example.BeFETest.entity.kospi;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "kospi_response")
public class KospiResponse {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String date;

    @Column(name = "current_price", nullable = false)
    private double currentPrice;

    @Column(name = "allday_ratio", nullable = false)
    private double allDayRatio;

    @Column(name = "percent_change", nullable = false)
    private double percentChange;

    @OneToMany(mappedBy = "response", cascade = CascadeType.ALL)
    private List<KospiEntity> kospiData; // KospiEntity를 리스트로 가짐


}
