package com.example.BeFETest.Repository;

import com.example.BeFETest.Entity.BacktestingHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BacktestingHistoryRepository extends JpaRepository<BacktestingHistory, Long> {
    List<BacktestingHistory> findFirst10ByOrderByDateDesc();
}
