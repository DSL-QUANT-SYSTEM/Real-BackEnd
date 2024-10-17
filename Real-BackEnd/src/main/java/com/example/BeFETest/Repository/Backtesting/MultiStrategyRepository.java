package com.example.BeFETest.Repository.Backtesting;

import com.example.BeFETest.Entity.BacktestingRes.IndicatorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MultiStrategyRepository extends JpaRepository<IndicatorEntity, Long> {
}
