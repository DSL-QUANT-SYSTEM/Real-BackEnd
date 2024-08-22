package com.example.BeFETest.DTO.coinDTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class GoldenDeadCrossStrategyDTO extends TradeStrategyDTO{

    private Long userId;
    private int fastMoveAvg;
    private int slowMoveAvg;



    public GoldenDeadCrossStrategyDTO(double initial_investment, double tax, LocalDate start_date, LocalDate end_date,
                                      String target_item, String tick_kind, int inq_range, String strategy, double finalCash, double finalAsset, double finalBalance,
                                      double profit, double profitRate, int numberOfTrades,
                                      int fastMoveAvg, int slowMoveAvg) {
        super(initial_investment, tax, start_date, end_date, target_item, tick_kind, inq_range, strategy,finalCash, finalAsset, finalBalance,
                profit, profitRate, numberOfTrades);
        this.fastMoveAvg = fastMoveAvg;
        this.slowMoveAvg = slowMoveAvg;
    }



    /**
     * TradingStrategyDTO goldenDeadCross = new GoldenDeadCrossStrategyDTO(10000, 0.01, LocalDate.of(2023, 1, 1), LocalDate.of(2024, 1, 1),
     *                                                                       "AAPL", "day", 30,
     *                                                                       5, 20);
     */

    @Override
    public String toString(){
        return "GoldenDeadCrossStrategyDTO{" +
                "initial_investment=" + initial_investment +
                ", tax=" + tax +
                ", start_date=" + start_date +
                ", end_date=" + end_date +
                ", target_item='" + target_item + '\'' +
                ", tick_kind='" + tick_kind + '\'' +
                ", inq_range=" + inq_range +
                ", fastMoveAvg=" + fastMoveAvg +
                ", slowMoveAvg=" + slowMoveAvg +
                ", finalCash=" + finalCash +
                ", finalAsset=" + finalAsset +
                ", finalBalance=" + finalBalance +
                ", profit=" + profit +
                ", profitRate=" + profitRate +
                ", numberOfTrades=" + numberOfTrades +
                '}';
    }

}
