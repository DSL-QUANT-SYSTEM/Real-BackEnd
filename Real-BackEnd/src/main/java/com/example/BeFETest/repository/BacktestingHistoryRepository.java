package com.example.BeFETest.repository;

import com.example.BeFETest.entity.BacktestingHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BacktestingHistoryRepository extends JpaRepository<BacktestingHistory, Long> {

    List<BacktestingHistory> findFirst10ByOrderByDateDesc();
}
