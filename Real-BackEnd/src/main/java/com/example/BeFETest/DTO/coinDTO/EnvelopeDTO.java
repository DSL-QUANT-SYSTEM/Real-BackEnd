package com.example.BeFETest.DTO.coinDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class EnvelopeDTO extends TradeStrategyDTO{

    private Long userId;
    private double moving_up;
    private double moving_down;
    private int movingAveragePeriod;

    public EnvelopeDTO(double initial_investment, double tax,  LocalDateTime backtesting_date,
                       String target_item, String tick_kind, int inq_range, String strategy,double finalCash, double finalAsset, double finalBalance,
                       double profit, double profitRate, int numberOfTrades,
                       double moving_down, double moving_up, int movingAveragePeriod) {
        super(initial_investment, tax,  backtesting_date, target_item, tick_kind, inq_range, strategy,finalCash, finalAsset, finalBalance,
                profit, profitRate, numberOfTrades);
        this.moving_down=moving_down;
        this.moving_up=moving_up;
        this.movingAveragePeriod=movingAveragePeriod;
    }
}
