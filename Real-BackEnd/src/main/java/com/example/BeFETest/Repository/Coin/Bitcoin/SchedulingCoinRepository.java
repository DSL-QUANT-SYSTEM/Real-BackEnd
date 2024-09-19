package com.example.BeFETest.Repository.Coin.Bitcoin;

import com.example.BeFETest.Entity.SchedulingCoin.SchedulingCoinResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SchedulingCoinRepository extends JpaRepository<SchedulingCoinResponse,Long> {

    List<SchedulingCoinResponse> findTop20ByOrderByIdDesc();
}
