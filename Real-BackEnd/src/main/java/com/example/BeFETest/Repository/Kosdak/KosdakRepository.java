package com.example.BeFETest.Repository.Kosdak;

import com.example.BeFETest.Entity.kosdak.KosdakResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface KosdakRepository extends JpaRepository<KosdakResponse, Long> {

    KosdakResponse findByDate(LocalDate date);

//    @Query("SELECT K FROM KosdakResponse K WHERE TRIM(K.date) >= :oneYearAgo")
//    List<KosdakResponse> findResponsesByDate(@Param("oneYearAgo") String oneYearAgo);

    //@Query("SELECT K FROM KosdakResponse K WHERE K.date >= :startDate AND K.date <= :endDate")
    //List<KosdakResponse> findResponsesWithinDateRange(@Param("startDate") String startDate, @Param("endDate") String endDate);
}
