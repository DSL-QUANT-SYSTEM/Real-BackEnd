package com.example.BeFETest.repository;

import com.example.BeFETest.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByBirthDate(String birthDate);
}
