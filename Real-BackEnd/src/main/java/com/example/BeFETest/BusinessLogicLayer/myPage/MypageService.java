package com.example.BeFETest.BusinessLogicLayer.myPage;

import com.example.BeFETest.DTO.kakaoDTO.Account;
import com.example.BeFETest.DTO.user.UserDTO;
import com.example.BeFETest.Entity.BacktestingRes.*;
import com.example.BeFETest.JWT.JwtUtil;
import com.example.BeFETest.Repository.Backtesting.*;
import com.example.BeFETest.Repository.kakao.accountRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
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
    private final MultiStrategyRepository multiStrategyRepository;


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
        if(results != null && !results.isEmpty()){
            return results.stream().collect(Collectors.toList());
        }else{
            return null;
        }

    }

    public List<BBEntity> getTop10BB(String token){
        Long userId = jwtUtil.getUserIdFromToken(token);
        List<BBEntity> results = bbRepository.findTop10ByUserIdOrderByIdDesc(userId);
        if(results != null && !results.isEmpty()){
            return results.stream().collect(Collectors.toList());
        }else{
            return null;
        }
    }

    public List<EnvEntity> getTop10Env(String token){
        Long userId = jwtUtil.getUserIdFromToken(token);
        List<EnvEntity> results = envRepository.findTop10ByUserIdOrderByIdDesc(userId);
        if(results != null && !results.isEmpty()){
            return results.stream().collect(Collectors.toList());
        }else{
            return null;
        }
    }

    public List<WEntity> getTop10W(String token){
        Long userId = jwtUtil.getUserIdFromToken(token);
        List<WEntity> results = wRepository.findTop10ByUserIdOrderByIdDesc(userId);
        if(results != null && !results.isEmpty()){
            return results.stream().collect(Collectors.toList());
        }else{
            return null;
        }
    }

    public List<IndicatorEntity> getTop10Indi(String token){
        Long userId = jwtUtil.getUserIdFromToken(token);
        List<IndicatorEntity> results = indicatorRepository.findTop10ByUserIdOrderByIdDesc(userId);
        if(results != null && !results.isEmpty()){
            return results.stream().collect(Collectors.toList());
        }else{
            return null;
        }
    }


    public List<GDEntity> getAllGD(String token){
        Long userId = jwtUtil.getUserIdFromToken(token);
        List<GDEntity> results = gdRepository.findByUserIdOrderByIdDesc(userId);
        if(results != null && !results.isEmpty()){
            return results.stream().collect(Collectors.toList());
        }else{
            return null;
        }

    }

    public List<BBEntity> getAllBB(String token){
        Long userId = jwtUtil.getUserIdFromToken(token);
        List<BBEntity> results = bbRepository.findByUserIdOrderByIdDesc(userId);
        if(results != null && !results.isEmpty()){
            return results.stream().collect(Collectors.toList());
        }else{
            return null;
        }
    }

    public List<EnvEntity> getAllEnv(String token){
        Long userId = jwtUtil.getUserIdFromToken(token);
        List<EnvEntity> results = envRepository.findByUserIdOrderByIdDesc(userId);
        if(results != null && !results.isEmpty()){
            return results.stream().collect(Collectors.toList());
        }else{
            return null;
        }
    }

    public List<WEntity> getAllW(String token){
        Long userId = jwtUtil.getUserIdFromToken(token);
        List<WEntity> results = wRepository.findByUserIdOrderByIdDesc(userId);
        if(results != null && !results.isEmpty()){
            return results.stream().collect(Collectors.toList());
        }else{
            return null;
        }
    }

    public List<IndicatorEntity> getAllIndi(String token){
        Long userId = jwtUtil.getUserIdFromToken(token);
        List<IndicatorEntity> results = indicatorRepository.findByUserIdOrderByIdDesc(userId);
        if(results != null && !results.isEmpty()){
            return results.stream().collect(Collectors.toList());
        }else{
            return null;
        }
    }

    public List<MultiStrategyEntity> getAllMulti(String token){
        Long userId = jwtUtil.getUserIdFromToken(token);
        List<MultiStrategyEntity> results = multiStrategyRepository.findByUserIdOrderByIdDesc(userId);
        if(results != null && !results.isEmpty()){
            return results.stream().collect(Collectors.toList());
        }else{
            return null;
        }
    }


}
