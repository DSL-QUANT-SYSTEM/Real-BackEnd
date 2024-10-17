package com.example.BeFETest.DTO.coinDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MultiStrategyDTO extends TradeStrategyDTO{
    private Long userId;
    private int moveAvg;

    private double moving_up;
    private double moving_down;
    private int movingAveragePeriod;

    private int fastMoveAvg;
    private int slowMoveAvg;

    private int rsiPeriod;

    private int williamsPeriod;

    public MultiStrategyDTO(double initial_investment, double tax, LocalDateTime backtesting_date,
                                     String target_item, String tick_kind, int inq_range, String strategy,double finalCash, double finalAsset, double finalBalance,
                                     double profit, double profitRate, int numberOfTrades,
                                     int moveAvg, double moving_up, double moving_down, int movingAveragePeriod,
                            int fastMoveAvg, int slowMoveAvg, int rsiPeriod, int williamsPeriod) {
        super(initial_investment, tax, backtesting_date, target_item, tick_kind, inq_range, strategy, finalCash, finalAsset, finalBalance,
                profit, profitRate, numberOfTrades);
        this.moveAvg = moveAvg;
        this.moving_up = moving_up;
        this.moving_down = moving_down;
        this.movingAveragePeriod = movingAveragePeriod;
        this.fastMoveAvg = fastMoveAvg;
        this.slowMoveAvg = slowMoveAvg;
        this.rsiPeriod = rsiPeriod;
        this.williamsPeriod = williamsPeriod;
    }

}
