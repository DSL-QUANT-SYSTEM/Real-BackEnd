package com.example.BeFETest.Entity.BacktestingRes;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "golden_dead_cross")
public class GDEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    //옵션 정보들
    private double initial_investment;
    private double tax;
    private LocalDateTime backtesting_date;
    private String target_item;
    private String tick_kind;
    private int inq_range;
    private String strategy;
    private int fastMovingAveragePeriod;
    private int slowMovingAveragePeriod;


    //결과 정보들
    private double finalCash;
    private double finalAsset;
    private double finalBalance;
    private double profit;
    private double profitRate;
    private int numberOfTrades;

    @Override
    public String toString() {
        return "GDEntity{" +
                "id=" + id +
                ", userId=" + userId +
                ", initialInvestment=" + initial_investment +
                ", tax=" + tax +
                ", backtesting_date=" + backtesting_date +
                ", targetItem='" + target_item + '\'' +
                ", tickKind='" + tick_kind + '\'' +
                ", inquiryRange=" + inq_range +
                ", strategy=" + strategy +
                ", fastMovingAveragePeriod=" + fastMovingAveragePeriod +
                ", slowMovingAveragePeriod=" + slowMovingAveragePeriod +
                ", finalCash=" + finalCash +
                ", finalAsset=" + finalAsset+
                ", finalBalance=" + finalBalance+
                ", profit=" + profit+
                ", profitRate=" +profitRate+
                ", numberOfTrades=" + numberOfTrades
                ;
    }
}
