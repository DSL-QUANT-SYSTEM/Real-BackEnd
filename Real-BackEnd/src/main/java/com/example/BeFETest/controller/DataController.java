package com.example.BeFETest.controller;

import com.example.BeFETest.BusinessLogicLayer.coin.CoinService;
import com.example.BeFETest.BusinessLogicLayer.kosdaq.KosdaqResponseService;
import com.example.BeFETest.BusinessLogicLayer.kospi.Kospi200ResponseService;
import com.example.BeFETest.BusinessLogicLayer.kospi.KospiResponseService;
import com.example.BeFETest.DTO.SchedulingCoin.SchedulingCoinDTO;
import com.example.BeFETest.DTO.kakaoDTO.Account;
import com.example.BeFETest.DTO.kosdaq.KosdaqResponseDTO;
import com.example.BeFETest.DTO.kospi.KospiResponseDTO;
import com.example.BeFETest.DTO.kospi200.Kospi200ResponseDTO;
import com.example.BeFETest.DTO.user.UserDTO;
import com.example.BeFETest.JWT.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.example.BeFETest.Repository.kakao.accountRepo;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DataController {

    ///api/kosdak/last-year

    private final KosdaqResponseService kosdaqService;
    private final KospiResponseService kospiService;
    private final Kospi200ResponseService kospi200Service;

    private final CoinService coinService;
    //private final JwtUtil jwtUtil;

    //private final accountRepo accountRepo;

    @GetMapping("/home/kosdaq")
    public List<KosdaqResponseDTO> getKosdakYearData(){
        return kosdaqService.getResponsesByYear();
    }


    @GetMapping("/home/kospi")
    public List<KospiResponseDTO> getKospiYearData(){
        return kospiService.getResponsesByYear();
    }

    @GetMapping("/home/kospi200")
    public List<Kospi200ResponseDTO> getKospi200YearData(){
        return kospi200Service.getResponsesByYear();
    }



    @GetMapping("/home/top20")
    public List<SchedulingCoinDTO> getTop20Coin(){
        return coinService.getTop20CoinByFluctuating();
    }

    @GetMapping("/home/top20test")
    public List<SchedulingCoinDTO> getTop20Cointest(){
        return coinService.getTop20Coin();
    }
    

    /*

    @GetMapping("/userinfo")
    public ResponseEntity<?> getUserInfo(@RequestHeader("Authorization") String token){
        Long userId = jwtUtil.getUserIdFromToken(token);
        log.info("userId::{}", userId);

        Optional<Account> byId = accountRepo.findById(userId);


        UserDTO userDTO = new UserDTO(byId.get().getUsername(), byId.get().getEmail());

        String name = byId.get().getUsername();
        String email = byId.get().getEmail();

        log.info("name: {}, email: {}", name, email);

        return ResponseEntity.ok(userDTO);
    }
*/

    /*
    @GetMapping("/user/info")
    public ResponseEntity<UserDTO> getMyPage(@RequestHeader("Authorization") String token){
        Long userId = jwtUtil.getUserIdFromToken(token);
        Optional<Account> byId = accountRepo.findById(userId);


        UserDTO userDTO = new UserDTO(byId.get().getUsername(), byId.get().getEmail());

        String name = byId.get().getUsername();
        String email = byId.get().getEmail();

        log.info("name: {}, email: {}", name, email);

        return ResponseEntity.ok(userDTO);
    }
    */

}
