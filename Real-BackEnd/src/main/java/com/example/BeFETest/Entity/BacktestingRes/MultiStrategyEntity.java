package com.example.BeFETest.Entity.BacktestingRes;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class MultiStrategyEntity {
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

    //BBEntity
    private int moveAvg;

    //EnvEntity
    private double moving_up;
    private double moving_down;
    private int movingAveragePeriod;

    //GDEntity
    private int fastMovingAveragePeriod;
    private int slowMovingAveragePeriod;

    //Indicator
    private int rsiPeriod;

    //WEntity
    private int williamsPeriod;


    //결과 정보들
    private double finalCash;
    private double finalAsset;
    private double finalBalance;
    private double profit;
    private double profitRate;
    private int numberOfTrades;

    @Override
    public String toString() {
        return "BBEntity{" +
                "id=" + id +
                ", userId=" + userId +
                ", initialInvestment=" + initial_investment +
                ", tax=" + tax +
                ", backtesting_date=" + backtesting_date +
                ", targetItem='" + target_item + '\'' +
                ", tickKind='" + tick_kind + '\'' +
                ", inquiryRange=" + inq_range +
                ", strategy=" + strategy +
                ", moveAvg=" + moveAvg +
                ", moving_up=" + moving_up+
                ", moving_down=" + moving_down +
                ", movingAveragePeriod=" + movingAveragePeriod+
                ", fastMovingAveragePeriod=" + fastMovingAveragePeriod+
                ", slowMovingAveragePeriod=" + slowMovingAveragePeriod+
                ", rsiPeriod=" + rsiPeriod+
                ", williamsPeriod=" + williamsPeriod+
                ", finalCash=" + finalCash +
                ", finalAsset=" + finalAsset+
                ", finalBalance=" + finalBalance+
                ", profit=" + profit+
                ", profitRate=" +profitRate+
                ", numberOfTrades=" + numberOfTrades

                ;
    }
}
