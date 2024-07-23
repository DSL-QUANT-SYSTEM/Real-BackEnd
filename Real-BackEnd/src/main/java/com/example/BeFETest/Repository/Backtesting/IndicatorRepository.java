package com.example.BeFETest.Repository.Backtesting;

import com.example.BeFETest.Entity.BacktestingRes.IndicatorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IndicatorRepository extends JpaRepository<IndicatorEntity, Long> {

    List<IndicatorEntity> findByUserIdOrderByIdDesc(Long userId);
    List<IndicatorEntity> findTop10ByUserIdOrderByIdDesc(Long userId);
    IndicatorEntity findTopByUserIdOrderByIdDesc(Long userId);
}
