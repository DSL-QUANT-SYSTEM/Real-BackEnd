package com.example.BeFETest.DTO.coinDTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class StrategyCommonDTO {
    //입력정보
    private double initialInvestment;
    private double transactionFee;
    private LocalDate startDate;
    private LocalDate endDate;
    private String targetItem;
    private String tickKind;
    private int inquiryRange;
    //결과정보
    private double finalCash;
    private double finalAsset;
    private double finalBalance;
    private double profit;
    private double profitRate;
    private int numberOfTrades;

    public StrategyCommonDTO(double initialInvestment, double transactionFee,
                            LocalDate startDate, LocalDate endDate, String targetItem, String tickKind,
                            int inquiryRange, double finalCash, double finalAsset, double finalBalance,
                            double profit, double profitRate, int numberOfTrades) {
        this.initialInvestment = initialInvestment;
        this.transactionFee = transactionFee;
        this.startDate = startDate;
        this.endDate = endDate;
        this.targetItem = targetItem;
        this.tickKind = tickKind;
        this.inquiryRange = inquiryRange;

        this.finalCash=finalCash;
        this.finalAsset=finalAsset;
        this.finalBalance=finalBalance;
        this.profit=profit;
        this.profitRate=profitRate;
        this.numberOfTrades=numberOfTrades;
    }
}
