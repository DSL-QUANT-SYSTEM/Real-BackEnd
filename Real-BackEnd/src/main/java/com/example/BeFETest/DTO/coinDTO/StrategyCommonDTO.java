package com.example.BeFETest.DTO.coinDTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class StrategyCommonDTO {
    private double initialInvestment;
    private double transactionFee;
    private LocalDate startDate;
    private LocalDate endDate;
    private String targetItem;
    private String tickKind;
    private int inquiryRange;

    public StrategyCommonDTO(double initialInvestment, double transactionFee, LocalDate startDate, LocalDate endDate,
                             String targetItem, String tickKind, int inquiryRange) {
        this.initialInvestment = initialInvestment;
        this.transactionFee = transactionFee;
        this.startDate = startDate;
        this.endDate = endDate;
        this.targetItem = targetItem;
        this.tickKind = tickKind;
        this.inquiryRange = inquiryRange;
    }
}
