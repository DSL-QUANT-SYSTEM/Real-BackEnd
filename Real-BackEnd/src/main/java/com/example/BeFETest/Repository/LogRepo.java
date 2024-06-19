package com.example.BeFETest.Repository;


import com.example.BeFETest.Entity.LogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepo extends JpaRepository<LogEntity, Long> {

}
