package com.example.BeFETest.DTO.coinDTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class WilliamsDTO extends TradeStrategyDTO {

    private Long userId;

    private int williamsPeriod;

    public WilliamsDTO(double initial_investment, double tax,  LocalDateTime backtesting_date,
                       String target_item, String tick_kind, int inq_range, String strategy, double finalCash, double finalAsset, double finalBalance,
                       double profit, double profitRate, int numberOfTrades, int williamsPeriod) {
        super(initial_investment, tax,  backtesting_date, target_item, tick_kind, inq_range, strategy, finalCash, finalAsset, finalBalance,
                profit, profitRate, numberOfTrades);
        this.williamsPeriod = williamsPeriod;
    }
}