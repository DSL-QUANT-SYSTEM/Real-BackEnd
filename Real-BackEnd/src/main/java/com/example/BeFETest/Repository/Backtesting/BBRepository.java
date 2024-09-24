package com.example.BeFETest.Repository.Backtesting;

import com.example.BeFETest.Entity.BacktestingRes.BBEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BBRepository extends JpaRepository<BBEntity, Long> {

    List<BBEntity> findByUserIdOrderByIdDesc(Long userId);
    List<BBEntity> findTop10ByUserIdOrderByIdDesc(Long userId);
    List<BBEntity> findTop100ByUserIdOrderByIdDesc(Long userId);
    BBEntity findTopByUserIdOrderByIdDesc(Long userId);

    List<BBEntity> findAllByUserId(Long userId);
}
