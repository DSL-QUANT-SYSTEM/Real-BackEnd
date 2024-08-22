package com.example.BeFETest.DTO.coinDTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class BollingerBandsStrategyDTO extends TradeStrategyDTO{

    private Long userId;

    private int moveAvg;

    public BollingerBandsStrategyDTO(double initial_investment, double tax, LocalDate start_date, LocalDate end_date,
                                     String target_item, String tick_kind, int inq_range, String strategy,double finalCash, double finalAsset, double finalBalance,
                                     double profit, double profitRate, int numberOfTrades,
                                     int moveAvg) {
        super(initial_investment, tax, start_date, end_date, target_item, tick_kind, inq_range, strategy, finalCash, finalAsset, finalBalance,
                profit, profitRate, numberOfTrades);
        this.moveAvg = moveAvg;
    }


    /**
     * 사용 예시 ->  TradingStrategyDTO bollingerBands = new BollingerBandsStrategyDTO(10000, 0.01, LocalDate.of(2023, 1, 1), LocalDate.of(2024, 1, 1),
     *                                                                     "AAPL", "day", 30,
     *                                                                     20);
     *
     */
}
