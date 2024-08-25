package com.example.BeFETest.Repository.Kospi;


import com.example.BeFETest.Entity.kospi200.Kospi200Response;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface Kospi200Repository extends JpaRepository<Kospi200Response, Long> {

    Kospi200Response findByDate(LocalDate date);
}
