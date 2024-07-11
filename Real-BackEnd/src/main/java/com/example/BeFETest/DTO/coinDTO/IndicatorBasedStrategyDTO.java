package com.example.BeFETest.DTO.coinDTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class IndicatorBasedStrategyDTO extends TradeStrategyDTO {

    private int rsiPeriod;

    public IndicatorBasedStrategyDTO(double initialInvestment, double transactionFee, LocalDate startDate, LocalDate endDate,
                                  String targetItem, String tickKind, int inquiryRange,
                                  int rsiPeriod) {
        super(initialInvestment, transactionFee, startDate, endDate, targetItem, tickKind, inquiryRange);
        this.rsiPeriod = rsiPeriod;
    }


    /**
     * TradingStrategyDTO indicatorBased = new IndicatorBasedStrategyDTO(10000, 0.01, LocalDate.of(2023, 1, 1), LocalDate.of(2024, 1, 1),
     *                                                                     "AAPL", "day", 30,
     *                                                                     14, 5);
     *
     */
}
