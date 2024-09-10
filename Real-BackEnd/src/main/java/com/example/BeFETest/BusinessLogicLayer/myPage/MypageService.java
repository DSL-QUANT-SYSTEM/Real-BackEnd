package com.example.BeFETest.BusinessLogicLayer.myPage;

import com.example.BeFETest.DTO.kakaoDTO.Account;
import com.example.BeFETest.DTO.user.UserDTO;
import com.example.BeFETest.Entity.BacktestingRes.BBEntity;
import com.example.BeFETest.Entity.BacktestingRes.GDEntity;
import com.example.BeFETest.JWT.JwtUtil;
import com.example.BeFETest.Repository.Backtesting.*;
import com.example.BeFETest.Repository.kakao.accountRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MypageService {

    private final JwtUtil jwtUtil;
    private final accountRepo repository;
    private final GDRepository gdRepository;
    private final BBRepository bbRepository;
    private final IndicatorRepository indicatorRepository;

    private final EnvRepository envRepository;
    private final WRepository wRepository;


    public UserDTO getUserInfo(String token){
        Long userId = jwtUtil.getUserIdFromToken(token);
        Optional<Account> byId = repository.findById(userId);

        if(!byId.isPresent()){
            throw new IllegalArgumentException("해당 사용자 없음");
        }
        return new UserDTO(byId.get().getUsername(), byId.get().getEmail());
    }

    public List<GDEntity> getTop10GD(String token){
        Long userId = jwtUtil.getUserIdFromToken(token);
        List<GDEntity> results = gdRepository.findTop10ByUserIdOrderByIdDesc(userId);
        if(results != null){
            return results.stream().collect(Collectors.toList());
        }else{
            return null;
        }
    }

    public List<BBEntity> getTop10BB(String token){
        Long userId = jwtUtil.getUserIdFromToken(token);
        List<BBEntity> results = bbRepository.findTop10ByUserIdOrderByIdDesc(userId);
        if(results != null){
            return results.stream().collect(Collectors.toList());
        }else{
            return null;
        }
    }


}
