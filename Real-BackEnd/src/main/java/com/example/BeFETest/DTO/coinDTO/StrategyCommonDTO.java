package com.example.BeFETest.DTO.coinDTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class StrategyCommonDTO {
    //입력정보
    private double initial_investment;
    private double tax;
    private LocalDateTime backtesting_date;
    private String target_item;
    private String tick_kind;
    private int inq_range;
    private String strategy;
    //결과정보
    private double finalCash;
    private double finalAsset;
    private double finalBalance;
    private double profit;
    private double profitRate;
    private int numberOfTrades;

    public StrategyCommonDTO(double initial_investment, double tax, LocalDateTime backtesting_date,String target_item, String tick_kind,
                            int inq_range, String strategy, double finalCash, double finalAsset,
                             double finalBalance, double profit, double profitRate, int numberOfTrades) {
        this.initial_investment = initial_investment;
        this.tax = tax;
        this.backtesting_date=backtesting_date;
        this.target_item = target_item;
        this.tick_kind = tick_kind;
        this.inq_range = inq_range;
        this.strategy=strategy;

        this.finalCash=finalCash;
        this.finalAsset=finalAsset;
        this.finalBalance=finalBalance;
        this.profit=profit;
        this.profitRate=profitRate;
        this.numberOfTrades=numberOfTrades;

    }

    @Override
    public String toString() {
        return "StrategyCommonDTO{" +
                "initialInvestment=" + initial_investment +
                ", tax=" + tax +
                ", target_item='" + target_item + '\'' +
                ", tick_kind='" + tick_kind + '\'' +
                ", inq_range=" + inq_range +
                ", strategy=" + strategy
//                ", finalCash=" + finalCash +
//                ", finalAsset=" + finalAsset+
//                ", finalBalance=" + finalBalance+
//                ", profit=" + profit+
//                ", profitRate=" +profitRate+
//                ", numberOfTrades=" + numberOfTrades
                ;
    }
}
