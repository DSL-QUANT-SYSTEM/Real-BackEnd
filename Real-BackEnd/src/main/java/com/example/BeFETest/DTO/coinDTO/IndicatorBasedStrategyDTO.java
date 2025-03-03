package com.example.BeFETest.DTO.coinDTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class IndicatorBasedStrategyDTO extends TradeStrategyDTO {

    private Long userId;

    private int rsiPeriod;

    public IndicatorBasedStrategyDTO(double initial_investment, double tax,  LocalDateTime backtesting_date,
                                     String target_item, String tick_kind, int inq_range,String strategy, double finalCash, double finalAsset, double finalBalance,
                                     double profit, double profitRate, int numberOfTrades, int rsiPeriod) {
        super(initial_investment, tax, backtesting_date, target_item, tick_kind, inq_range, strategy,finalCash, finalAsset, finalBalance,
                profit, profitRate, numberOfTrades);
        this.rsiPeriod = rsiPeriod;
    }


    /**
     * TradingStrategyDTO indicatorBased = new IndicatorBasedStrategyDTO(10000, 0.01, LocalDate.of(2023, 1, 1), LocalDate.of(2024, 1, 1),
     *                                                                     "AAPL", "day", 30,
     *                                                                     14, 5);
     *
     */
}
