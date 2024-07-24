package com.example.BeFETest.DTO.coinDTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class IndicatorBasedStrategyDTO extends TradeStrategyDTO {

    private Long userId;

    private int rsiPeriod;

    public IndicatorBasedStrategyDTO(double initialInvestment, double transactionFee, LocalDate startDate, LocalDate endDate,
                                  String targetItem, String tickKind, int inquiryRange, double finalCash, double finalAsset, double finalBalance,
                                  double profit, double profitRate, int numberOfTrades, int rsiPeriod) {
        super(initialInvestment, transactionFee, startDate, endDate, targetItem, tickKind, inquiryRange, finalCash, finalAsset, finalBalance, profit, profitRate, numberOfTrades);
        this.rsiPeriod = rsiPeriod;
    }


    /**
     * TradingStrategyDTO indicatorBased = new IndicatorBasedStrategyDTO(10000, 0.01, LocalDate.of(2023, 1, 1), LocalDate.of(2024, 1, 1),
     *                                                                     "AAPL", "day", 30,
     *                                                                     14, 5);
     *
     */
}
