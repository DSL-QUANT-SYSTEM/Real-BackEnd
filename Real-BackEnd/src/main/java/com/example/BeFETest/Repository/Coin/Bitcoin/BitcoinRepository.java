package com.example.BeFETest.Repository.Coin.Bitcoin;

import com.example.BeFETest.Entity.Bitcoin.BitcoinResponse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BitcoinRepository extends JpaRepository<BitcoinResponse, Long> {
}
