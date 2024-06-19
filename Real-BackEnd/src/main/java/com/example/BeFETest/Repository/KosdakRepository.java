package com.example.BeFETest.Repository;

import com.example.BeFETest.Entity.kosdak.KosdakResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KosdakRepository extends JpaRepository<KosdakResponse, Long> {

    List<KosdakResponse> findByDate(String date);
}
