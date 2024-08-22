package com.example.BeFETest.Repository.Kospi;


import com.example.BeFETest.Entity.kospi.KospiResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface KospiRepository extends JpaRepository<KospiResponse, Long> {

    @Query("SELECT K FROM KospiResponse K WHERE TRIM(K.date) >= :oneYearAgo")
    List<KospiResponse> findResponsesByDate(@Param("oneYearAgo") String oneYearAgo);

    //@Query("SELECT k FROM KospiResponse k WHERE k.date = :date")
    //List<KospiResponse> findByDate(@Param("date") LocalDate date);
    KospiResponse findByDate(String date);
}
