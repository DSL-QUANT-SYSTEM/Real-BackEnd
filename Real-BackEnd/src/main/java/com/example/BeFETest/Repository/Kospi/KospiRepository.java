package com.example.BeFETest.Repository.Kospi;


import com.example.BeFETest.Entity.kospi.KospiResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface KospiRepository extends JpaRepository<KospiResponse, Long> {

    //@Query("SELECT k FROM KospiResponse k WHERE k.date = :date")
    //List<KospiResponse> findByDate(@Param("date") LocalDate date);
    KospiResponse findByDate(LocalDate date);
}
