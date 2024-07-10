package com.example.BeFETest.repository;


import com.example.BeFETest.entity.kospi.KospiResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KospiRepository extends JpaRepository<KospiResponse, Long> {

    //@Query("SELECT k FROM KospiResponse k WHERE k.date = :date")
    //List<KospiResponse> findByDate(@Param("date") LocalDate date);
    List<KospiResponse> findByDate(String date);
}
