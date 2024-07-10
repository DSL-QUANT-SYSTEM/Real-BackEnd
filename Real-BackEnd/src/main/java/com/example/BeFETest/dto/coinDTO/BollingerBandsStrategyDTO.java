package com.example.BeFETest.dto.coinDTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class BollingerBandsStrategyDTO extends TradeStrategyDTO{

    private int movingAveragePeriod;

    public BollingerBandsStrategyDTO(double initialInvestment, double transactionFee, LocalDate startDate, LocalDate endDate,
                                     String targetItem, String tickKind, int inquiryRange,
                                     int movingAveragePeriod) {
        super(initialInvestment, transactionFee, startDate, endDate, targetItem, tickKind, inquiryRange);
        this.movingAveragePeriod = movingAveragePeriod;
    }


    /**
     * 사용 예시 ->  TradingStrategyDTO bollingerBands = new BollingerBandsStrategyDTO(10000, 0.01, LocalDate.of(2023, 1, 1), LocalDate.of(2024, 1, 1),
     *                                                                     "AAPL", "day", 30,
     *                                                                     20);
     *
     */
}
