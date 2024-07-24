package com.example.BeFETest.Entity.BacktestingRes;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "bollinger_band")
public class BBEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    //옵션 정보들
    private double initialInvestment;
    private double transactionFee;
    private LocalDate startDate;
    private LocalDate endDate;
    private String targetItem;
    private String tickKind;
    private int inquiryRange;
    private int movingAveragePeriod;


    //결과 정보들
    private double finalCash;
    private double finalAsset;
    private double finalBalance;
    private double profit;
    private double profitRate;
    private int numberOfTrades;
}
