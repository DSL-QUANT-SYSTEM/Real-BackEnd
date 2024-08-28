package com.example.BeFETest.Repository.Kosdaq;

import com.example.BeFETest.Entity.kosdaq.KosdaqResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface KosdaqRepository extends JpaRepository<KosdaqResponse, Long> {

    KosdaqResponse findByDate(LocalDate date);

//    @Query("SELECT K FROM KosdakResponse K WHERE TRIM(K.date) >= :oneYearAgo")
//    List<KosdakResponse> findResponsesByDate(@Param("oneYearAgo") String oneYearAgo);

    //@Query("SELECT K FROM KosdakResponse K WHERE K.date >= :startDate AND K.date <= :endDate")
    //List<KosdakResponse> findResponsesWithinDateRange(@Param("startDate") String startDate, @Param("endDate") String endDate);
}
