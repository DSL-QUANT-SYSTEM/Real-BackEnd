package com.example.BeFETest.DTO.coinDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class EnvelopeDTO extends TradeStrategyDTO{

    private double moving_up;
    private double moving_down;
    private int movingAveragePeriod;

    public EnvelopeDTO(double initialInvestment, double tax, LocalDate start_date, LocalDate end_date,
                       String target_item, String tick_kind, int inq_range, double finalCash, double finalAsset, double finalBalance,
                       double profit, double profitRate, int numberOfTrades,
                       double moving_down, double moving_up, int movingAveragePeriod) {
        super(initialInvestment, tax, start_date, end_date, target_item, tick_kind, inq_range);
        this.moving_down=moving_down;
        this.moving_up=moving_up;
        this.movingAveragePeriod=movingAveragePeriod;
    }
}
