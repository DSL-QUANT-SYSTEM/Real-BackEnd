package com.example.BeFETest.Repository.Kospi;


import com.example.BeFETest.Entity.kospi200.Kospi200Response;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface Kospi200Repository extends JpaRepository<Kospi200Response, Long> {

//    @Query("SELECT K FROM Kospi200Response K WHERE TRIM(K.date) >= :oneYearAgo")
//    List<Kospi200Response> findResponsesByDate(@Param("oneYearAgo") String oneYearAgo);

    @Query("SELECT K FROM Kospi200Response K WHERE K.date >= :startDate AND K.date <= :endDate")
    List<Kospi200Response> findResponsesWithinDateRange(@Param("startDate") String startDate, @Param("endDate") String endDate);


    List<Kospi200Response> findByDateBetween(LocalDate startDate, LocalDate endDate);

    Kospi200Response findByDate(LocalDate date);
}

