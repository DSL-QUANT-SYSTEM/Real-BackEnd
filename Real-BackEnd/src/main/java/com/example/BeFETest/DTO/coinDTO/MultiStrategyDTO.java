package com.example.BeFETest.DTO.coinDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MultiStrategyDTO extends TradeStrategyDTO{
    private Long userId;

    private String second_strategy;

    private double profitVsRate; //첫 전략 기준 2번째 전략과의 수익률 비율 결과
    private double finalProfitRate; // 복합 최종 수익률

    private int moveAvg;

    private double moving_up;
    private double moving_down;
    private int movingAveragePeriod;

    private int fastMoveAvg;
    private int slowMoveAvg;

    private int rsiPeriod;

    private int williamsPeriod;

    private double second_finalCash;
    private double second_finalAsset;
    private double second_finalBalance;
    private double second_profit;
    private double second_profitRate;
    private int second_numberOfTrades;

    public MultiStrategyDTO(double initial_investment, double tax, LocalDateTime backtesting_date,
                                     String target_item, String tick_kind, int inq_range, String strategy, String second_strategy, double finalCash, double finalAsset, double finalBalance,
                                     double profit, double profitRate, int numberOfTrades,
                                     int moveAvg, double moving_up, double moving_down, int movingAveragePeriod,
                            int fastMoveAvg, int slowMoveAvg, int rsiPeriod, int williamsPeriod ,
                            double second_finalCash, double second_finalAsset, double second_finalBalance,
                            double second_profit, double second_profitRate, int second_numberOfTrades, double profitVsRate, double finalProfitRate) {
        super(initial_investment, tax, backtesting_date, target_item, tick_kind, inq_range, strategy, finalCash, finalAsset, finalBalance,
                profit, profitRate, numberOfTrades);
        this.second_strategy=second_strategy;
        this.moveAvg = moveAvg;
        this.moving_up = moving_up;
        this.moving_down = moving_down;
        this.movingAveragePeriod = movingAveragePeriod;
        this.fastMoveAvg = fastMoveAvg;
        this.slowMoveAvg = slowMoveAvg;
        this.rsiPeriod = rsiPeriod;
        this.williamsPeriod = williamsPeriod;
        this.second_finalCash=second_finalCash;
        this.second_finalAsset=second_finalAsset;
        this.second_finalBalance=second_finalBalance;
        this.second_profit=second_profit;
        this.second_profitRate=second_profitRate;
        this.second_numberOfTrades=second_numberOfTrades;
        this.profitVsRate=profitVsRate;
        this.finalProfitRate=finalProfitRate;
    }

    @Override
    public String toString() {
        return "MulEntity{" +
                ", userId=" + userId +
                ", initialInvestment=" + initial_investment +
                ", tax=" + tax +
                ", targetItem='" + target_item + '\'' +
                ", tickKind='" + tick_kind + '\'' +
                ", inquiryRange=" + inq_range +
                ", secondStrategy='" + second_strategy + '\'' + // 추가된 필드
                ", moveAvg=" + moveAvg +
                ", movingUp=" + moving_up + // 수정된 필드 이름
                ", movingDown=" + moving_down + // 수정된 필드 이름
                ", movingAveragePeriod=" + movingAveragePeriod +
                ", fastMovingAveragePeriod=" + fastMoveAvg +
                ", slowMovingAveragePeriod=" + slowMoveAvg +
                ", rsiPeriod=" + rsiPeriod +
                ", williamsPeriod=" + williamsPeriod +
                ", finalCash=" + finalCash +
                ", finalAsset=" + finalAsset +
                ", finalBalance=" + finalBalance +
                ", profit=" + profit +
                ", profitRate=" + profitRate +
                ", numberOfTrades=" + numberOfTrades +
                ", secondFinalCash=" + second_finalCash + // 추가된 필드
                ", secondFinalAsset=" + second_finalAsset + // 추가된 필드
                ", secondFinalBalance=" + second_finalBalance + // 추가된 필드
                ", secondProfit=" + second_profit + // 추가된 필드
                ", secondProfitRate=" + second_profitRate + // 추가된 필드
                ", secondNumberOfTrades=" + second_numberOfTrades + // 추가된 필드
                ", profitVsRate=" + profitVsRate + // 추가된 필드
                ", finalProfitRate=" + finalProfitRate + // 추가된 필드
                '}';
    }

}
