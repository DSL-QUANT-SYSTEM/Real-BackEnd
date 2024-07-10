package com.example.BeFETest.repository;

import com.example.BeFETest.entity.kosdak.KosdakResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KosdakRepository extends JpaRepository<KosdakResponse, Long> {

    List<KosdakResponse> findByDate(String date);
}
