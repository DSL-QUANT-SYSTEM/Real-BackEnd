package com.example.BeFETest.Repository;


import com.example.BeFETest.Entity.kosdak2000.Kosdak2000Response;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Kosdak2000Repository extends JpaRepository<Kosdak2000Response, Long> {

    List<Kosdak2000Response> findByDate(String date);
}
