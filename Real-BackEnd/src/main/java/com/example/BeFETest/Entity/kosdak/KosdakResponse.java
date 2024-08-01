package com.example.BeFETest.Entity.kosdak;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "kosdak_response")
public class KosdakResponse {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String date;

    @Column(name = "closing_price", nullable = false)
    private double closingPrice;

    @Column(name = "comparison", nullable = false)
    private double comparison;

    @Column(name = "fluctuation_rate", nullable = false)
    private double fluctuationRate;

    @Column(name = "opening_price", nullable = false)
    private double openingPrice;

    @Column(name = "high_price", nullable = false)
    private double highPrice;

    @Column(name = "low_price", nullable = false)
    private double lowPrice;

    @Column(name = "trading_volume", nullable = false)
    private double tradingVolume;

    @Column(name = "trading_amount", nullable = false)
    private double tradingAmount;

    @Column(name = "listed_capitalization", nullable = false)
    private double listedCapitalization;

}
