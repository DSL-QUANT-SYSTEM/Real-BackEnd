package com.example.BeFETest.DTO.coinDTO;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
@Getter
@Setter
public abstract class TradeStrategyDTO {
    //입력정보
    protected double initial_investment;
    protected double tax;
    private LocalDateTime backtesting_date;
    protected String target_item;
    protected String tick_kind;
    protected int inq_range;
    private String strategy;

    //결과정보
    protected double finalCash;
    protected double finalAsset;
    protected double finalBalance;
    protected double profit;
    protected double profitRate;
    protected int numberOfTrades;
    public TradeStrategyDTO(double initial_investment, double tax, LocalDateTime backtesting_date, String target_item, String tick_kind,
                            int inq_range, String strategy, double finalCash, double finalAsset,
                            double finalBalance, double profit, double profitRate, int numberOfTrades) {
        this.initial_investment = initial_investment;
        this.tax = tax;
        this.backtesting_date=backtesting_date;
        this.target_item = target_item;
        this.tick_kind = tick_kind;
        this.inq_range = inq_range;
        this.strategy=strategy;

        this.finalCash=finalCash;
        this.finalAsset=finalAsset;
        this.finalBalance=finalBalance;
        this.profit=profit;
        this.profitRate=profitRate;
        this.numberOfTrades=numberOfTrades;

    }

}
