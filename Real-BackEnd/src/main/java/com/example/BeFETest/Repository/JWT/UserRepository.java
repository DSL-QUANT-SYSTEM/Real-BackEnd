package com.example.BeFETest.Repository.JWT;

import com.example.BeFETest.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByBirthDate(String birthDate);
}
