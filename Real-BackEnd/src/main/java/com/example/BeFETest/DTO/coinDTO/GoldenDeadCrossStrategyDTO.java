package com.example.BeFETest.DTO.coinDTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class GoldenDeadCrossStrategyDTO extends TradeStrategyDTO{

    private Long userId;
    private int fastMovingAveragePeriod;
    private int slowMovingAveragePeriod;



    public GoldenDeadCrossStrategyDTO(double initialInvestment, double transactionFee, LocalDate startDate, LocalDate endDate,
                                      String targetItem, String tickKind, int inquiryRange,
                                      int fastMovingAveragePeriod, int slowMovingAveragePeriod) {
        super(initialInvestment, transactionFee, startDate, endDate, targetItem, tickKind, inquiryRange);
        this.fastMovingAveragePeriod = fastMovingAveragePeriod;
        this.slowMovingAveragePeriod = slowMovingAveragePeriod;
    }

    /**
     * TradingStrategyDTO goldenDeadCross = new GoldenDeadCrossStrategyDTO(10000, 0.01, LocalDate.of(2023, 1, 1), LocalDate.of(2024, 1, 1),
     *                                                                       "AAPL", "day", 30,
     *                                                                       5, 20);
     */

    @Override
    public String toString(){
        return "GoldenDeadCrossStrategyDTO{" +
                "initialInvestment=" + initialInvestment +
                ", transactionFee=" + transactionFee +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", targetItem='" + targetItem + '\'' +
                ", tickKind='" + tickKind + '\'' +
                ", inquiryRange=" + inquiryRange +
                ", fastMovingAveragePeriod=" + fastMovingAveragePeriod +
                ", slowMovingAveragePeriod=" + slowMovingAveragePeriod +
                '}';
    }

}
