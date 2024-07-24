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

    public EnvelopeDTO(double initialInvestment, double transactionFee, LocalDate startDate, LocalDate endDate,
                       String targetItem, String tickKind, int inquiryRange, double finalCash, double finalAsset, double finalBalance,
                       double profit, double profitRate, int numberOfTrades,
                       double moving_down, double moving_up, int movingAveragePeriod) {
        super(initialInvestment, transactionFee, startDate, endDate, targetItem, tickKind, inquiryRange, finalCash, finalAsset, finalBalance, profit, profitRate, numberOfTrades);
        this.moving_down=moving_down;
        this.moving_up=moving_up;
        this.movingAveragePeriod=movingAveragePeriod;
    }
}
