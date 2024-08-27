package com.example.BeFETest.Entity.BacktestingRes;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "envelope")
public class EnvEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    //옵션 정보들
    private double initial_investment;
    private double tax;
    private LocalDate start_date;
    private LocalDate end_date;
    private String target_item;
    private String tick_kind;
    private int inq_range;
    private String strategy;
    private double moving_up;
    private double moving_down;
    private int movingAveragePeriod;


    //결과 정보들
    private double finalCash;
    private double finalAsset;
    private double finalBalance;
    private double profit;
    private double profitRate;
    private int numberOfTrades;

    @Override
    public String toString() {
        return "EnvEntity{" +
                "id=" + id +
                ", userId=" + userId +
                ", initialInvestment=" + initial_investment +
                ", tax=" + tax +
                ", startDate=" + start_date +
                ", endDate=" + end_date +
                ", targetItem='" + target_item + '\'' +
                ", tickKind='" + tick_kind + '\'' +
                ", inquiryRange=" + inq_range +
                ", strategy=" + strategy +
                ", moving_up=" + moving_up +
                ", moving_down=" + moving_down +
                ", movingAveragePeriod=" + movingAveragePeriod +
                ", finalCash=" + finalCash +
                ", finalAsset=" + finalAsset+
                ", finalBalance=" + finalBalance+
                ", profit=" + profit+
                ", profitRate=" +profitRate+
                ", numberOfTrades=" + numberOfTrades
                ;
    }
}
