package com.example.BeFETest.Repository.Backtesting;

import com.example.BeFETest.Entity.BacktestingRes.IndicatorEntity;
import com.example.BeFETest.Entity.BacktestingRes.MultiStrategyEntity;
import com.example.BeFETest.Entity.BacktestingRes.WEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MultiStrategyRepository extends JpaRepository<MultiStrategyEntity, Long> {
    List<MultiStrategyEntity> findByUserIdOrderByIdDesc(Long userId);
    List<MultiStrategyEntity> findTop10ByUserIdOrderByIdDesc(Long userId);
    List<MultiStrategyEntity> findTop100ByUserIdOrderByIdDesc(Long userId);
    MultiStrategyEntity findTopByUserIdOrderByIdDesc(Long userId);
    List<MultiStrategyEntity> findAllByUserId(Long userId);
}
