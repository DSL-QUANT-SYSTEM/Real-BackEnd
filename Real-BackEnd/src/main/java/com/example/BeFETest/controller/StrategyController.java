package com.example.BeFETest.controller;

import com.example.BeFETest.BusinessLogicLayer.Strategy.StrategyService;
import com.example.BeFETest.DTO.coinDTO.*;
import com.example.BeFETest.JWT.JwtUtil;
import com.example.BeFETest.Strategy.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class StrategyController {

    @Autowired
    private final StrategyService strategyService;

    @Autowired
    private JwtUtil jwtUtil;


    @PostMapping("/strategy")
    public ResponseEntity<?> saveCommonStrategy(@RequestHeader("Authorization") String token, @RequestBody StrategyCommonDTO strategyCommonDTO) {
        String updatedToken = jwtUtil.addCommonStrategyDataToken(token, strategyCommonDTO);


        ResponseEntity<?> response = ResponseEntity.ok().header("Authorization", updatedToken)
                .body("Common strategy saved successfully");

        return response;

    }

    @PostMapping("/strategy/golden")
    public ResponseEntity<?> saveGDStrategy(@RequestHeader("Authorization") String token, @RequestBody GoldenDeadCrossStrategyDTO gdStrategyDTO) {
        StrategyCommonDTO commonDTO = jwtUtil.extractCommonStrategyDataFromToken(token);
        if (commonDTO == null) {
            return ResponseEntity.status(400).body("Common strategy must be set before using this endpoint.");
        }

        Long userId = jwtUtil.getUserIdFromToken(token);
        gdStrategyDTO.setUserId(userId);

        // Backtesting 실행
        GoldenDeadCrossStrategyDTO gdResultDTO = BacktestingGD.executeTrades(commonDTO, gdStrategyDTO);
        strategyService.saveGDStrategyResult(commonDTO, userId, gdStrategyDTO, gdResultDTO);
        // gdEntity가 null인지 체크

        return ResponseEntity.ok("GD strategy saved successfully");
    }


    @PostMapping("/strategy/bollinger")
    public ResponseEntity<?> saveBBStrategy(@RequestHeader("Authorization") String token, @RequestBody BollingerBandsStrategyDTO bbStrategyDTO) {
        StrategyCommonDTO commonDTO = jwtUtil.extractCommonStrategyDataFromToken(token);
        if (commonDTO == null) {
            return ResponseEntity.status(400).body("Common strategy must be set before using this endpoint.");
        }

        Long userId = jwtUtil.getUserIdFromToken(token);
        bbStrategyDTO.setUserId(userId);

        BollingerBandsStrategyDTO bbResultDTO = BacktestingBB.executeTrades(commonDTO, bbStrategyDTO);
        strategyService.saveBBStrategyResult(commonDTO, userId, bbStrategyDTO, bbResultDTO);

        return ResponseEntity.ok("BB strategy saved successfully");
    }

    @PostMapping("/strategy/rsi")
    public ResponseEntity<?> saveRSIStrategy(@RequestHeader("Authorization") String token, @RequestBody IndicatorBasedStrategyDTO indicatorDTO) {
        StrategyCommonDTO commonDTO = jwtUtil.extractCommonStrategyDataFromToken(token);
        if (commonDTO == null) {
            return ResponseEntity.status(400).body("Common strategy must be set before using this endpoint.");
        }

        Long userId = jwtUtil.getUserIdFromToken(token);
        indicatorDTO.setUserId(userId);

        IndicatorBasedStrategyDTO indResultDTO = BacktestingIndicator.executeTrades(commonDTO, indicatorDTO);
        strategyService.saveIndicatorStrategyResult(commonDTO, userId, indicatorDTO, indResultDTO);

        return ResponseEntity.ok("Indicator strategy saved successfully");
    }

    @PostMapping("/strategy/env")
    public ResponseEntity<?> saveEnvStrategy(@RequestHeader("Authorization") String token, @RequestBody EnvelopeDTO envelopeDTO) {

        StrategyCommonDTO commonDTO = jwtUtil.extractCommonStrategyDataFromToken(token);

        if (commonDTO == null) {
            return ResponseEntity.status(400).body("Common strategy must be set before using this endpoint.");
        }

        Long userId = jwtUtil.getUserIdFromToken(token);
        envelopeDTO.setUserId(userId);

        EnvelopeDTO envResultDTO = BacktestingEnv.executeTrades(commonDTO, envelopeDTO);
        strategyService.saveEnvelopeStrategyResult(commonDTO, userId, envelopeDTO, envResultDTO);
        return ResponseEntity.ok("Env strategy saved successfully");
    }

    @PostMapping("/strategy/williams")
    public ResponseEntity<?> saveWStrategy(@RequestHeader("Authorization") String token, @RequestBody WilliamsDTO williamsDTO) {
        StrategyCommonDTO commonDTO = jwtUtil.extractCommonStrategyDataFromToken(token);
        if (commonDTO == null) {
            return ResponseEntity.status(400).body("Common strategy must be set before using this endpoint.");
        }

        Long userId = jwtUtil.getUserIdFromToken(token);
        williamsDTO.setUserId(userId);

        WilliamsDTO williamsResultDTO = BacktestingW.executeTrades(commonDTO, williamsDTO);
        strategyService.saveWilliamsStrategyResult(commonDTO, userId, williamsDTO, williamsResultDTO);
        return ResponseEntity.ok("Williams strategy saved successfully");
    }




    @GetMapping("/result/golden")
    public ResponseEntity<?> getGDStrategyResult(@RequestHeader("Authorization") String token){
        Long userId = jwtUtil.getUserIdFromToken(token);
        GoldenDeadCrossStrategyDTO gdResultDTO = strategyService.getLatestGDStrategyResultByUserId(userId);
        return ResponseEntity.ok(gdResultDTO);
    }
    @GetMapping("/result/bollinger")
    public ResponseEntity<?> getBBStrategyResult(@RequestHeader("Authorization") String token){
        Long userId = jwtUtil.getUserIdFromToken(token);
        BollingerBandsStrategyDTO bbResultDTO = strategyService.getLatestBBStrategyResultByUserId(userId);
        return ResponseEntity.ok(bbResultDTO);
    }
    @GetMapping("/result/rsi")
    public ResponseEntity<?> getIndicatorStrategyResult(@RequestHeader("Authorization") String token){
        Long userId = jwtUtil.getUserIdFromToken(token);
        IndicatorBasedStrategyDTO indResultDTO = strategyService.getLatestIndicatorStrategyResultByUserId(userId);
        return ResponseEntity.ok(indResultDTO);
    }
    @GetMapping("/result/env")
    public ResponseEntity<?> getEnvStrategyResult(@RequestHeader("Authorization") String token){
        Long userId = jwtUtil.getUserIdFromToken(token);
        EnvelopeDTO envelopeDTO = strategyService.getLatestEnvStrategyResultByUserId(userId);
        return ResponseEntity.ok(envelopeDTO);
    }
    @GetMapping("/result/williams")
    public ResponseEntity<?> getWilliamsStrategyResult(@RequestHeader("Authorization") String token){
        Long userId = jwtUtil.getUserIdFromToken(token);
        WilliamsDTO williamsDTO = strategyService.getLatestWilliamsStrategyResultByUserId(userId);
        return ResponseEntity.ok(williamsDTO);
    }

}