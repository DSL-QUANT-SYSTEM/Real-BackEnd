package com.example.BeFETest.Repository.Backtesting;

import com.example.BeFETest.Entity.BacktestingRes.GDEntity;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface GDRepository extends JpaRepository<GDEntity, Long> {

    List<GDEntity> findByUserIdOrderByIdDesc(Long userId);
    List<GDEntity> findTop10ByUserIdOrderByIdDesc(Long userId);
    GDEntity findTopByUserIdOrderByIdDesc(Long userId);
}
