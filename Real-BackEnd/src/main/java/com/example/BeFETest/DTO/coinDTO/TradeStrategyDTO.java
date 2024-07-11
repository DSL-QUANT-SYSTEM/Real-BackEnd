package com.example.BeFETest.DTO.coinDTO;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
@Getter
@Setter
public abstract class TradeStrategyDTO {

    protected double initialInvestment;
    protected double transactionFee;
    protected LocalDate startDate;
    protected LocalDate endDate;
    protected String targetItem;
    protected String tickKind;
    protected int inquiryRange;

    public TradeStrategyDTO(double initialInvestment, double transactionFee,
                            LocalDate startDate, LocalDate endDate, String targetItem, String tickKind, int inquiryRange){
        this.initialInvestment = initialInvestment;
        this.transactionFee = transactionFee;
        this.startDate = startDate;
        this.endDate = endDate;
        this.targetItem = targetItem;
        this.tickKind = tickKind;
        this.inquiryRange = inquiryRange;
    }

}
