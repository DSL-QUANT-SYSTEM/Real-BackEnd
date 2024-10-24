package com.example.BeFETest.Entity.BacktestingRes;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "multi_strategy")
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
    private String second_strategy;

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

    private double second_finalCash;
    private double second_finalAsset;
    private double second_finalBalance;
    private double second_profit;
    private double second_profitRate;
    private int second_numberOfTrades;

    private double profitVsRate; //첫 전략 기준 2번째 전략과의 수익률 비율 결과
    private double finalProfitRate; // 복합 최종 비율

    @Override
    public String toString() {
        return "MulEntity{" +
                "id=" + id +
                ", userId=" + userId +
                ", initialInvestment=" + initial_investment +
                ", tax=" + tax +
                ", backtestingDate=" + backtesting_date +
                ", targetItem='" + target_item + '\'' +
                ", tickKind='" + tick_kind + '\'' +
                ", inquiryRange=" + inq_range +
                ", strategy='" + strategy + '\'' +
                ", secondStrategy='" + second_strategy + '\'' + // 추가된 필드
                ", moveAvg=" + moveAvg +
                ", movingUp=" + moving_up + // 수정된 필드 이름
                ", movingDown=" + moving_down + // 수정된 필드 이름
                ", movingAveragePeriod=" + movingAveragePeriod +
                ", fastMovingAveragePeriod=" + fastMovingAveragePeriod +
                ", slowMovingAveragePeriod=" + slowMovingAveragePeriod +
                ", rsiPeriod=" + rsiPeriod +
                ", williamsPeriod=" + williamsPeriod +
                ", finalCash=" + finalCash +
                ", finalAsset=" + finalAsset +
                ", finalBalance=" + finalBalance +
                ", profit=" + profit +
                ", profitRate=" + profitRate +
                ", numberOfTrades=" + numberOfTrades +
                ", secondFinalCash=" + second_finalCash + // 추가된 필드
                ", secondFinalAsset=" + second_finalAsset + // 추가된 필드
                ", secondFinalBalance=" + second_finalBalance + // 추가된 필드
                ", secondProfit=" + second_profit + // 추가된 필드
                ", secondProfitRate=" + second_profitRate + // 추가된 필드
                ", secondNumberOfTrades=" + second_numberOfTrades + // 추가된 필드
                ", profitVsRate=" + profitVsRate + // 추가된 필드
                ", finalProfitRate=" + finalProfitRate + // 추가된 필드
                '}';
    }
}
