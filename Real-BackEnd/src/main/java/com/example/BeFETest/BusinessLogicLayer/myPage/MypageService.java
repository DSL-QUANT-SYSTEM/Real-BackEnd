package com.example.BeFETest.BusinessLogicLayer.myPage;

import com.example.BeFETest.DTO.kakaoDTO.Account;
import com.example.BeFETest.DTO.user.UserDTO;
import com.example.BeFETest.JWT.JwtUtil;
import com.example.BeFETest.Repository.kakao.accountRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MypageService {

    private final JwtUtil jwtUtil;

    private final accountRepo repository;

    public UserDTO getUserInfo(String token){
        Long userId = jwtUtil.getUserIdFromToken(token);
        Optional<Account> byId = repository.findById(userId);

        if(!byId.isPresent()){
            throw new IllegalArgumentException("해당 사용자 없음");
        }
        return new UserDTO(byId.get().getUsername(), byId.get().getEmail());
    }


}
