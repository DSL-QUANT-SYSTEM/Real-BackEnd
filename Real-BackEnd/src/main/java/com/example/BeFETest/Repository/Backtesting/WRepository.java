package com.example.BeFETest.Repository.Backtesting;

import com.example.BeFETest.Entity.BacktestingRes.WEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WRepository extends JpaRepository<WEntity, Long>{
    List<WEntity> findByUserIdOrderByIdDesc(Long userId);
    List<WEntity> findTop10ByUserIdOrderByIdDesc(Long userId);
    WEntity findTopByUserIdOrderByIdDesc(Long userId);
    List<WEntity> findAllByUserId(Long userId);
}
