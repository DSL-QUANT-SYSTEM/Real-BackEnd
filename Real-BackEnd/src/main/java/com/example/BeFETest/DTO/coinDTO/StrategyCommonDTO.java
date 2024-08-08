package com.example.BeFETest.DTO.coinDTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class StrategyCommonDTO {
    //입력정보
    private double initial_investment;
    private double tax;
    private LocalDate start_date;
    private LocalDate end_date;
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

    public StrategyCommonDTO(double initial_investment, double tax,
                            LocalDate start_date, LocalDate end_date, String target_item, String tick_kind,
                            int inq_range, String strategy) {
        this.initial_investment = initial_investment;
        this.tax = tax;
        this.start_date = start_date;
        this.end_date = end_date;
        this.target_item = target_item;
        this.tick_kind = tick_kind;
        this.inq_range = inq_range;
        this.strategy=strategy;
    }

    @Override
    public String toString() {
        return "StrategyCommonDTO{" +
                "initialInvestment=" + initial_investment +
                ", tax=" + tax +
                ", start_date=" + start_date +
                ", end_date=" + end_date +
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
