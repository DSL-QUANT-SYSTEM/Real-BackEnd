package com.example.BeFETest.Repository.kakao;

import com.example.BeFETest.DTO.kakaoDTO.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface accountRepo extends JpaRepository<Account, Long> {
    // 필요한 추가 메서드를 정의할 수 있습니다
    Optional<Account> findByKakaoId(Long kakaoId);
}

