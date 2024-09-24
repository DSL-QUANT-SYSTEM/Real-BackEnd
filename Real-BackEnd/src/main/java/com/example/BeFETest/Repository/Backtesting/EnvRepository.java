package com.example.BeFETest.Repository.Backtesting;

import com.example.BeFETest.Entity.BacktestingRes.EnvEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnvRepository extends JpaRepository<EnvEntity, Long>{
    List<EnvEntity> findByUserIdOrderByIdDesc(Long userId);
    List<EnvEntity> findTop10ByUserIdOrderByIdDesc(Long userId);
    List<EnvEntity> findTop100ByUserIdOrderByIdDesc(Long userId);
    EnvEntity findTopByUserIdOrderByIdDesc(Long userId);
    List<EnvEntity> findAllByUserId(Long userId);
}
