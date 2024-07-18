package com.example.BeFETest.BusinessLogicLayer.Strategy;

import com.example.BeFETest.DTO.coinDTO.GoldenDeadCrossStrategyDTO;
import com.example.BeFETest.Entity.BacktestingRes.GDEntity;
import com.example.BeFETest.Repository.Backtesting.GDRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StrategyService {

    @Autowired
    private GDRepository gdRepository;

    @Transactional
    public void saveGDStrategyResult(GoldenDeadCrossStrategyDTO strategyDTO){
        GDEntity gdEntity = new GDEntity();
        gdEntity.setUserId(strategyDTO.getUserId());
        gdEntity.setInitialInvestment(strategyDTO.getInitialInvestment());
        gdEntity.setTransactionFee(strategyDTO.getTransactionFee());
        gdEntity.setStartDate(strategyDTO.getStartDate());
        gdEntity.setEndDate(strategyDTO.getEndDate());
        gdEntity.setTickKind(strategyDTO.getTickKind());
        gdEntity.setInquiryRange(strategyDTO.getInquiryRange());
        gdEntity.setFastMovingAveragePeriod(strategyDTO.getFastMovingAveragePeriod());
        gdEntity.setSlowMovingAveragePeriod(strategyDTO.getSlowMovingAveragePeriod());
    }
}
