package com.example.BeFETest.DTO.coinDTO;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
@Getter
@Setter
public abstract class TradeStrategyDTO {
    //입력정보
    protected double initialInvestment;
    protected double transactionFee;
    protected LocalDate startDate;
    protected LocalDate endDate;
    protected String targetItem;
    protected String tickKind;
    protected int inquiryRange;

    //결과정보
    protected double finalCash;
    protected double finalAsset;
    protected double finalBalance;
    protected double profit;
    protected double profitRate;
    protected int numberOfTrades;
    public TradeStrategyDTO(double initialInvestment, double transactionFee,
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
