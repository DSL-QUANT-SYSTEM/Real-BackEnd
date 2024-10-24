package com.example.BeFETest.controller;

import com.example.BeFETest.BusinessLogicLayer.Strategy.StrategyService;
import com.example.BeFETest.DTO.coinDTO.*;
import com.example.BeFETest.JWT.JwtUtil;
import com.example.BeFETest.Strategy.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class StrategyController {

    @Autowired
    private final StrategyService strategyService;

    @Autowired
    private JwtUtil jwtUtil;


    @Mapper
    public interface StrategyMapper {
        StrategyMapper INSTANCE = Mappers.getMapper(StrategyMapper.class);

        GoldenDeadCrossStrategyDTO toGoldenDeadStrategyDTO(MultiStrategyDTO multiStrategyDTO);
        BollingerBandsStrategyDTO toBollingerStrategyDTO(MultiStrategyDTO multiStrategyDTO);
        IndicatorBasedStrategyDTO toIndicatorStrategyDTO(MultiStrategyDTO multiStrategyDTO);
        EnvelopeDTO toEnvelopeDTO(MultiStrategyDTO multiStrategyDTO);
        WilliamsDTO toWilliamsDTO(MultiStrategyDTO multiStrategyDTO);
        MultiStrategyDTO toMultiStrategyDTO(GoldenDeadCrossStrategyDTO goldenDeadCrossStrategyDTO);
        MultiStrategyDTO toMultiStrategyDTO(BollingerBandsStrategyDTO bollingerBandsStrategyDTO);
        MultiStrategyDTO toMultiStrategyDTO(IndicatorBasedStrategyDTO indicatorBasedStrategyDTO);
        MultiStrategyDTO toMultiStrategyDTO(EnvelopeDTO envelopeDTO);
        MultiStrategyDTO toMultiStrategyDTO(WilliamsDTO williamsDTO);
    }

    public static double rebalancing(double return1, double return2, double initialInvestment) {
        double rebalance1 = 0;
        double rebalance2 = 0;

        // Case 1: 둘 다 양수일 경우, 첫 번째 수익률을 기준으로 비율 계산
        if (return1 > 0 && return2 > 0) {
            double totalReturn = return1 + return2;
            rebalance1 = initialInvestment * (return1 / totalReturn);
            return rebalance1;
            // Case 2: 하나만 양수고 나머지 하나는 0이거나 음수일 경우
        } else if (return1 > 0 && return2 <= 0) {
            rebalance1 = initialInvestment;
            return  rebalance1;
        } else if (return2 > 0 && return1 <= 0) {
            return 0;
            // Case 3: 둘 다 음수거나 0인 경우
        } else {
            if (Math.abs(return1) < Math.abs(return2)) {
                rebalance1 = initialInvestment;
                return rebalance1;
            } else {
                return 0;
            }
        }
    }

    @PostMapping("/strategy")
    public ResponseEntity<?> saveCommonStrategy(@RequestHeader("Authorization") String token, @RequestBody StrategyCommonDTO strategyCommonDTO) {
        String updatedToken = jwtUtil.addCommonStrategyDataToken(token, strategyCommonDTO);


        ResponseEntity<?> response = ResponseEntity.ok().header("Authorization", updatedToken)
                .body("Common strategy saved successfully");

        return response;

    }
    @PostMapping("/strategy/golden")
    public ResponseEntity<?> saveGDStrategy(@RequestHeader("Authorization") String token,
                                            @RequestBody GoldenDeadCrossStrategyDTO gdStrategyDTO){
        // Token에서 공통 전략 정보 추출
        StrategyCommonDTO commonDTO = jwtUtil.extractCommonStrategyDataFromToken(token);
        Long userId = jwtUtil.getUserIdFromToken(token);

        GoldenDeadCrossStrategyDTO gdResultDTO = BacktestingGD.executeTrades(commonDTO, gdStrategyDTO);
        strategyService.saveGDStrategyResult(commonDTO, userId, gdStrategyDTO, gdResultDTO);
        return ResponseEntity.ok("BB strategy saved successfully");
    }
    @PostMapping("/strategy/bollinger")
    public ResponseEntity<?> saveBBStrategy(@RequestHeader("Authorization") String token,
                                            @RequestBody BollingerBandsStrategyDTO bbStrategyDTO){
        // Token에서 공통 전략 정보 추출
        StrategyCommonDTO commonDTO = jwtUtil.extractCommonStrategyDataFromToken(token);
        Long userId = jwtUtil.getUserIdFromToken(token);

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

    @PostMapping("/strategy/golden/{second_strategy}")
    public ResponseEntity<?> saveGDMStrategy(@RequestHeader("Authorization") String token,
                                             @RequestBody CombinedStrategyDTO combinedStrategyDTO,
                                             @PathVariable("second_strategy") String secondStrategy){
        // Token에서 공통 전략 정보 추출
        StrategyCommonDTO commonDTO = jwtUtil.extractCommonStrategyDataFromToken(token);
        //CombineStrategyDTO에서 각 전략 정보 추출
        GoldenDeadCrossStrategyDTO gdStrategyDTO= combinedStrategyDTO.getGoldenStrategyDTO();
        if (commonDTO == null) {
            return ResponseEntity.status(400).body("Common strategy must be set before using this endpoint.");
        }
        double initial_investment= commonDTO.getInitial_investment(); //최종 수익률 계산하기 위해 초기값 저장

        Long userId = jwtUtil.getUserIdFromToken(token);
        MultiStrategyDTO multiStrategyDTO=combinedStrategyDTO.getMultiStrategyDTO();
        GoldenDeadCrossStrategyDTO gdResultDTO = BacktestingGD.executeTrades(commonDTO, gdStrategyDTO);

        switch (secondStrategy) {
            case "rsi" -> {
                IndicatorBasedStrategyDTO indStrategyDTO = StrategyMapper.INSTANCE.toIndicatorStrategyDTO(multiStrategyDTO);
                IndicatorBasedStrategyDTO indResultDTO = BacktestingIndicator.executeTrades(commonDTO, indStrategyDTO);
                double rebal1 = rebalancing(gdResultDTO.getProfitRate(), indResultDTO.getProfitRate(), commonDTO.getInitial_investment());
                double rebal2 = commonDTO.getInitial_investment() - rebal1;

                double profitVsRate = rebal1 / (rebal1 + rebal2);

                commonDTO.setInitial_investment(rebal1); //밸런싱 후 첫 전략에 자본 할당

                GoldenDeadCrossStrategyDTO gdResultDTO2 = BacktestingGD.executeTrades(commonDTO, gdStrategyDTO);
                commonDTO.setInitial_investment(rebal2); //밸런싱 후 두번 째 전략에 자본 할당

                IndicatorBasedStrategyDTO indResultDTO2 = BacktestingIndicator.executeTrades(commonDTO, indStrategyDTO);

                double finalProfitRate = 100 * ((gdResultDTO2.getFinalBalance() + indResultDTO2.getFinalBalance() - initial_investment) / initial_investment);
                commonDTO.setInitial_investment(initial_investment);

                MultiStrategyDTO mulStrategyGD = StrategyMapper.INSTANCE.toMultiStrategyDTO(gdStrategyDTO);
                MultiStrategyDTO mulResultGD = StrategyMapper.INSTANCE.toMultiStrategyDTO(gdResultDTO2);
                MultiStrategyDTO mulResultInd = StrategyMapper.INSTANCE.toMultiStrategyDTO(indResultDTO2);

                strategyService.saveMultiStrategyResult(commonDTO, userId, "golden", "rsi", mulStrategyGD, multiStrategyDTO, mulResultGD, mulResultInd, profitVsRate, finalProfitRate);
            }
            case "williams" -> {
                WilliamsDTO wStrategyDTO = StrategyMapper.INSTANCE.toWilliamsDTO(multiStrategyDTO);
                WilliamsDTO wResultDTO = BacktestingW.executeTrades(commonDTO, wStrategyDTO);
                double rebal1 = rebalancing(gdResultDTO.getProfitRate(), wResultDTO.getProfitRate(), commonDTO.getInitial_investment());
                double rebal2 = commonDTO.getInitial_investment() - rebal1;

                double profitVsRate = rebal1 / (rebal1 + rebal2);

                commonDTO.setInitial_investment(rebal1); //밸런싱 후 첫 전략에 자본 할당

                GoldenDeadCrossStrategyDTO gdResultDTO2 = BacktestingGD.executeTrades(commonDTO, gdStrategyDTO);
                commonDTO.setInitial_investment(rebal2); //밸런싱 후 두번 째 전략에 자본 할당

                WilliamsDTO wResultDTO2 = BacktestingW.executeTrades(commonDTO, wStrategyDTO);

                double finalProfitRate = 100 * ((gdResultDTO2.getFinalBalance() + wResultDTO2.getFinalBalance() - initial_investment) / initial_investment);
                commonDTO.setInitial_investment(initial_investment);

                MultiStrategyDTO mulStrategyGD = StrategyMapper.INSTANCE.toMultiStrategyDTO(gdStrategyDTO);
                MultiStrategyDTO mulResultGD = StrategyMapper.INSTANCE.toMultiStrategyDTO(gdResultDTO2);
                MultiStrategyDTO mulResultW = StrategyMapper.INSTANCE.toMultiStrategyDTO(wResultDTO2);

                strategyService.saveMultiStrategyResult(commonDTO, userId, "golden", "williams", mulStrategyGD, multiStrategyDTO, mulResultGD, mulResultW, profitVsRate, finalProfitRate);
            }
        }
        return ResponseEntity.ok("strategy saved successfully");
    }


    @PostMapping("/strategy/bollinger/{second_strategy}")
    public ResponseEntity<?> saveBBMStrategy(@RequestHeader("Authorization") String token,
                                            @RequestBody CombinedStrategyDTO combinedStrategyDTO,
                                            @PathVariable("second_strategy") String secondStrategy) {
        // Token에서 공통 전략 정보 추출
        StrategyCommonDTO commonDTO = jwtUtil.extractCommonStrategyDataFromToken(token);
        //CombineStrategyDTO에서 각 전략 정보 추출
        BollingerBandsStrategyDTO bbStrategyDTO= combinedStrategyDTO.getBbStrategyDTO();
        if (commonDTO == null) {
            return ResponseEntity.status(400).body("Common strategy must be set before using this endpoint.");
        }
        double initial_investment= commonDTO.getInitial_investment(); //최종 수익률 계산하기 위해 초기값 저장

        Long userId = jwtUtil.getUserIdFromToken(token);
        MultiStrategyDTO multiStrategyDTO=combinedStrategyDTO.getMultiStrategyDTO();
        BollingerBandsStrategyDTO bbResultDTO = BacktestingBB.executeTrades(commonDTO, bbStrategyDTO);
        switch (secondStrategy) {
            case "golden" -> {
                GoldenDeadCrossStrategyDTO gdStrategyDTO = StrategyMapper.INSTANCE.toGoldenDeadStrategyDTO(multiStrategyDTO);
                GoldenDeadCrossStrategyDTO gdResultDTO = BacktestingGD.executeTrades(commonDTO, gdStrategyDTO);
                double rebal1 = rebalancing(bbResultDTO.getProfitRate(), gdResultDTO.getProfitRate(), commonDTO.getInitial_investment());
                double rebal2 = commonDTO.getInitial_investment() - rebal1;

                double profitVsRate = rebal1 / (rebal1 + rebal2);

                commonDTO.setInitial_investment(rebal1); //밸런싱 후 첫 전략에 자본 할당

                BollingerBandsStrategyDTO bbResultDTO2 = BacktestingBB.executeTrades(commonDTO, bbStrategyDTO);
                commonDTO.setInitial_investment(rebal2); //밸런싱 후 두번 째 전략에 자본 할당

                GoldenDeadCrossStrategyDTO gdResultDTO2 = BacktestingGD.executeTrades(commonDTO, gdStrategyDTO);

                double finalProfitRate = 100 * ((bbResultDTO2.getFinalBalance() + gdResultDTO2.getFinalBalance() - initial_investment) / initial_investment);
                commonDTO.setInitial_investment(initial_investment);

                MultiStrategyDTO mulStrategyBB = StrategyMapper.INSTANCE.toMultiStrategyDTO(bbStrategyDTO);
                MultiStrategyDTO mulResultBB = StrategyMapper.INSTANCE.toMultiStrategyDTO(bbResultDTO2);
                MultiStrategyDTO mulResultGD = StrategyMapper.INSTANCE.toMultiStrategyDTO(gdResultDTO2);

                strategyService.saveMultiStrategyResult(commonDTO, userId, "bollinger", "golden", mulStrategyBB, multiStrategyDTO, mulResultBB, mulResultGD, profitVsRate, finalProfitRate);
            }
            case "rsi" -> {
                IndicatorBasedStrategyDTO indStrategyDTO = StrategyMapper.INSTANCE.toIndicatorStrategyDTO(multiStrategyDTO);
                IndicatorBasedStrategyDTO indResultDTO = BacktestingIndicator.executeTrades(commonDTO, indStrategyDTO);
                double rebal1 = rebalancing(bbResultDTO.getProfitRate(), indResultDTO.getProfitRate(), commonDTO.getInitial_investment());
                double rebal2 = commonDTO.getInitial_investment() - rebal1;

                double profitVsRate = rebal1 / (rebal1 + rebal2);

                commonDTO.setInitial_investment(rebal1); //밸런싱 후 첫 전략에 자본 할당

                BollingerBandsStrategyDTO bbResultDTO2 = BacktestingBB.executeTrades(commonDTO, bbStrategyDTO);
                commonDTO.setInitial_investment(rebal2); //밸런싱 후 두번 째 전략에 자본 할당

                IndicatorBasedStrategyDTO indResultDTO2 = BacktestingIndicator.executeTrades(commonDTO, indStrategyDTO);

                double finalProfitRate = 100 * ((bbResultDTO2.getFinalBalance() + indResultDTO2.getFinalBalance() - initial_investment) / initial_investment);
                commonDTO.setInitial_investment(initial_investment);

                MultiStrategyDTO mulStrategyBB = StrategyMapper.INSTANCE.toMultiStrategyDTO(bbStrategyDTO);
                MultiStrategyDTO mulResultBB = StrategyMapper.INSTANCE.toMultiStrategyDTO(bbResultDTO2);
                MultiStrategyDTO mulResultInd = StrategyMapper.INSTANCE.toMultiStrategyDTO(indResultDTO2);

                strategyService.saveMultiStrategyResult(commonDTO, userId, "bollinger", "rsi", mulStrategyBB, multiStrategyDTO, mulResultBB, mulResultInd, profitVsRate, finalProfitRate);
            }
            case "env" -> {
                EnvelopeDTO envStrategyDTO = StrategyMapper.INSTANCE.toEnvelopeDTO(multiStrategyDTO);
                EnvelopeDTO envResultDTO = BacktestingEnv.executeTrades(commonDTO, envStrategyDTO);
                double rebal1 = rebalancing(bbResultDTO.getProfitRate(), envResultDTO.getProfitRate(), commonDTO.getInitial_investment());
                double rebal2 = commonDTO.getInitial_investment() - rebal1;

                double profitVsRate = rebal1 / (rebal1 + rebal2);

                commonDTO.setInitial_investment(rebal1); //밸런싱 후 첫 전략에 자본 할당

                BollingerBandsStrategyDTO bbResultDTO2 = BacktestingBB.executeTrades(commonDTO, bbStrategyDTO);
                commonDTO.setInitial_investment(rebal2); //밸런싱 후 두번 째 전략에 자본 할당

                EnvelopeDTO envResultDTO2 = BacktestingEnv.executeTrades(commonDTO, envStrategyDTO);

                double finalProfitRate = 100 * ((bbResultDTO2.getFinalBalance() + envResultDTO2.getFinalBalance() - initial_investment) / initial_investment);
                commonDTO.setInitial_investment(initial_investment);

                MultiStrategyDTO mulStrategyBB = StrategyMapper.INSTANCE.toMultiStrategyDTO(bbStrategyDTO);
                MultiStrategyDTO mulResultBB = StrategyMapper.INSTANCE.toMultiStrategyDTO(bbResultDTO2);
                MultiStrategyDTO mulResultEnv = StrategyMapper.INSTANCE.toMultiStrategyDTO(envResultDTO2);

                strategyService.saveMultiStrategyResult(commonDTO, userId, "bollinger", "env", mulStrategyBB, multiStrategyDTO, mulResultBB, mulResultEnv, profitVsRate, finalProfitRate);
            }
            case "williams" -> {
                WilliamsDTO wStrategyDTO = StrategyMapper.INSTANCE.toWilliamsDTO(multiStrategyDTO);
                WilliamsDTO wResultDTO = BacktestingW.executeTrades(commonDTO, wStrategyDTO);
                double rebal1 = rebalancing(bbResultDTO.getProfitRate(), wResultDTO.getProfitRate(), commonDTO.getInitial_investment());
                double rebal2 = commonDTO.getInitial_investment() - rebal1;

                double profitVsRate = rebal1 / (rebal1 + rebal2);

                commonDTO.setInitial_investment(rebal1); //밸런싱 후 첫 전략에 자본 할당

                BollingerBandsStrategyDTO bbResultDTO2 = BacktestingBB.executeTrades(commonDTO, bbStrategyDTO);
                commonDTO.setInitial_investment(rebal2); //밸런싱 후 두번 째 전략에 자본 할당

                WilliamsDTO wResultDTO2 = BacktestingW.executeTrades(commonDTO, wStrategyDTO);

                double finalProfitRate = 100 * ((bbResultDTO2.getFinalBalance() + wResultDTO2.getFinalBalance() - initial_investment) / initial_investment);
                commonDTO.setInitial_investment(initial_investment);

                MultiStrategyDTO mulStrategyBB = StrategyMapper.INSTANCE.toMultiStrategyDTO(bbStrategyDTO);
                MultiStrategyDTO mulResultBB = StrategyMapper.INSTANCE.toMultiStrategyDTO(bbResultDTO2);
                MultiStrategyDTO mulResultW = StrategyMapper.INSTANCE.toMultiStrategyDTO(wResultDTO2);

                strategyService.saveMultiStrategyResult(commonDTO, userId, "bollinger", "williams", mulStrategyBB, multiStrategyDTO, mulResultBB, mulResultW, profitVsRate, finalProfitRate);
            }
        }
        return ResponseEntity.ok("Strategy saved successfully");
    }

    @PostMapping("/strategy/rsi/{second_strategy}")
    public ResponseEntity<?> saveRSIMStrategy(@RequestHeader("Authorization") String token,
                                              @RequestBody CombinedStrategyDTO combinedStrategyDTO,
                                              @PathVariable("second_strategy") String secondStrategy) {
        // Token에서 공통 전략 정보 추출
        StrategyCommonDTO commonDTO = jwtUtil.extractCommonStrategyDataFromToken(token);
        //CombineStrategyDTO에서 각 전략 정보 추출
        IndicatorBasedStrategyDTO indStrategyDTO= combinedStrategyDTO.getRsiStrategyDTO();
        if (commonDTO == null) {
            return ResponseEntity.status(400).body("Common strategy must be set before using this endpoint.");
        }
        double initial_investment= commonDTO.getInitial_investment(); //최종 수익률 계산하기 위해 초기값 저장

        // 복합 전략인 경우
        Long userId = jwtUtil.getUserIdFromToken(token);
        MultiStrategyDTO multiStrategyDTO=combinedStrategyDTO.getMultiStrategyDTO();
        IndicatorBasedStrategyDTO indResultDTO = BacktestingIndicator.executeTrades(commonDTO, indStrategyDTO);
        WilliamsDTO wStrategyDTO = StrategyMapper.INSTANCE.toWilliamsDTO(multiStrategyDTO);
        WilliamsDTO wResultDTO = BacktestingW.executeTrades(commonDTO, wStrategyDTO);
        double rebal1 = rebalancing(indResultDTO.getProfitRate(), wResultDTO.getProfitRate(), commonDTO.getInitial_investment());
        double rebal2 = commonDTO.getInitial_investment() - rebal1;

        double profitVsRate = rebal1 / (rebal1 + rebal2);

        commonDTO.setInitial_investment(rebal1); //밸런싱 후 첫 전략에 자본 할당

        IndicatorBasedStrategyDTO indResultDTO2 = BacktestingIndicator.executeTrades(commonDTO, indStrategyDTO);
        commonDTO.setInitial_investment(rebal2); //밸런싱 후 두번 째 전략에 자본 할당

        WilliamsDTO wResultDTO2 = BacktestingW.executeTrades(commonDTO, wStrategyDTO);

        double finalProfitRate = 100 * ((indResultDTO2.getFinalBalance() + wResultDTO2.getFinalBalance() - initial_investment) / initial_investment);
        commonDTO.setInitial_investment(initial_investment);

        MultiStrategyDTO mulStrategyInd = StrategyMapper.INSTANCE.toMultiStrategyDTO(indStrategyDTO);
        MultiStrategyDTO mulResultInd = StrategyMapper.INSTANCE.toMultiStrategyDTO(indResultDTO2);
        MultiStrategyDTO mulResultW = StrategyMapper.INSTANCE.toMultiStrategyDTO(wResultDTO2);

        strategyService.saveMultiStrategyResult(commonDTO, userId, "rsi", "williams", mulStrategyInd, multiStrategyDTO, mulResultInd, mulResultW, profitVsRate, finalProfitRate);

        return ResponseEntity.ok("Indicator strategy saved successfully");
    }

    @PostMapping("/strategy/env/{second_strategy}")
    public ResponseEntity<?> saveEnvMStrategy(@RequestHeader("Authorization") String token,
                                              @RequestBody CombinedStrategyDTO combinedStrategyDTO,
                                              @PathVariable("second_strategy") String secondStrategy) {

        // Token에서 공통 전략 정보 추출
        StrategyCommonDTO commonDTO = jwtUtil.extractCommonStrategyDataFromToken(token);
        //CombineStrategyDTO에서 각 전략 정보 추출
        EnvelopeDTO envStrategyDTO= combinedStrategyDTO.getEnvStrategyDTO();
        if (commonDTO == null) {
            return ResponseEntity.status(400).body("Common strategy must be set before using this endpoint.");
        }
        double initial_investment= commonDTO.getInitial_investment(); //최종 수익률 계산하기 위해 초기값 저장

        // 복합 전략인 경우
        Long userId = jwtUtil.getUserIdFromToken(token);
        MultiStrategyDTO multiStrategyDTO=combinedStrategyDTO.getMultiStrategyDTO();
        EnvelopeDTO envResultDTO = BacktestingEnv.executeTrades(commonDTO, envStrategyDTO);

        switch (secondStrategy) {
            case "golden" -> {
                GoldenDeadCrossStrategyDTO gdStrategyDTO = StrategyMapper.INSTANCE.toGoldenDeadStrategyDTO(multiStrategyDTO);
                GoldenDeadCrossStrategyDTO gdResultDTO = BacktestingGD.executeTrades(commonDTO, gdStrategyDTO);
                double rebal1 = rebalancing(envResultDTO.getProfitRate(), gdResultDTO.getProfitRate(), commonDTO.getInitial_investment());
                double rebal2 = commonDTO.getInitial_investment() - rebal1;

                double profitVsRate = rebal1 / (rebal1 + rebal2);

                commonDTO.setInitial_investment(rebal1); //밸런싱 후 첫 전략에 자본 할당

                EnvelopeDTO envResultDTO2 = BacktestingEnv.executeTrades(commonDTO, envStrategyDTO);
                commonDTO.setInitial_investment(rebal2); //밸런싱 후 두번 째 전략에 자본 할당

                GoldenDeadCrossStrategyDTO gdResultDTO2 = BacktestingGD.executeTrades(commonDTO, gdStrategyDTO);

                double finalProfitRate = 100 * ((envResultDTO2.getFinalBalance() + gdResultDTO2.getFinalBalance() - initial_investment) / initial_investment);
                commonDTO.setInitial_investment(initial_investment);

                MultiStrategyDTO mulStrategyEnv = StrategyMapper.INSTANCE.toMultiStrategyDTO(envStrategyDTO);
                MultiStrategyDTO mulResultEnv = StrategyMapper.INSTANCE.toMultiStrategyDTO(envResultDTO2);
                MultiStrategyDTO mulResultGD = StrategyMapper.INSTANCE.toMultiStrategyDTO(gdResultDTO2);

                strategyService.saveMultiStrategyResult(commonDTO, userId, "env", "golden", mulStrategyEnv, multiStrategyDTO, mulResultEnv, mulResultGD, profitVsRate, finalProfitRate);
            }
            case "rsi" -> {
                IndicatorBasedStrategyDTO indStrategyDTO = StrategyMapper.INSTANCE.toIndicatorStrategyDTO(multiStrategyDTO);
                IndicatorBasedStrategyDTO indResultDTO = BacktestingIndicator.executeTrades(commonDTO, indStrategyDTO);
                double rebal1 = rebalancing(envResultDTO.getProfitRate(), indResultDTO.getProfitRate(), commonDTO.getInitial_investment());
                double rebal2 = commonDTO.getInitial_investment() - rebal1;

                double profitVsRate = rebal1 / (rebal1 + rebal2);

                commonDTO.setInitial_investment(rebal1); //밸런싱 후 첫 전략에 자본 할당

                EnvelopeDTO envResultDTO2 = BacktestingEnv.executeTrades(commonDTO, envStrategyDTO);
                commonDTO.setInitial_investment(rebal2); //밸런싱 후 두번 째 전략에 자본 할당

                IndicatorBasedStrategyDTO indResultDTO2 = BacktestingIndicator.executeTrades(commonDTO, indStrategyDTO);

                double finalProfitRate = 100 * ((envResultDTO2.getFinalBalance() + indResultDTO2.getFinalBalance() - initial_investment) / initial_investment);
                commonDTO.setInitial_investment(initial_investment);

                MultiStrategyDTO mulStrategyEnv = StrategyMapper.INSTANCE.toMultiStrategyDTO(envStrategyDTO);
                MultiStrategyDTO mulResultEnv = StrategyMapper.INSTANCE.toMultiStrategyDTO(envResultDTO2);
                MultiStrategyDTO mulResultInd = StrategyMapper.INSTANCE.toMultiStrategyDTO(indResultDTO2);

                strategyService.saveMultiStrategyResult(commonDTO, userId, "env", "rsi", mulStrategyEnv, multiStrategyDTO, mulResultEnv, mulResultInd, profitVsRate, finalProfitRate);
            }
            case "williams" -> {
                WilliamsDTO wStrategyDTO = StrategyMapper.INSTANCE.toWilliamsDTO(multiStrategyDTO);
                WilliamsDTO wResultDTO = BacktestingW.executeTrades(commonDTO, wStrategyDTO);
                double rebal1 = rebalancing(envResultDTO.getProfitRate(), wResultDTO.getProfitRate(), commonDTO.getInitial_investment());
                double rebal2 = commonDTO.getInitial_investment() - rebal1;

                double profitVsRate = rebal1 / (rebal1 + rebal2);

                commonDTO.setInitial_investment(rebal1); //밸런싱 후 첫 전략에 자본 할당

                EnvelopeDTO envResultDTO2 = BacktestingEnv.executeTrades(commonDTO, envStrategyDTO);
                commonDTO.setInitial_investment(rebal2); //밸런싱 후 두번 째 전략에 자본 할당

                WilliamsDTO wResultDTO2 = BacktestingW.executeTrades(commonDTO, wStrategyDTO);

                double finalProfitRate = 100 * ((envResultDTO2.getFinalBalance() + wResultDTO2.getFinalBalance() - initial_investment) / initial_investment);
                commonDTO.setInitial_investment(initial_investment);

                MultiStrategyDTO mulStrategyEnv = StrategyMapper.INSTANCE.toMultiStrategyDTO(envStrategyDTO);
                MultiStrategyDTO mulResultEnv = StrategyMapper.INSTANCE.toMultiStrategyDTO(envResultDTO2);
                MultiStrategyDTO mulResultW = StrategyMapper.INSTANCE.toMultiStrategyDTO(wResultDTO2);

                strategyService.saveMultiStrategyResult(commonDTO, userId, "env", "williams", mulStrategyEnv, multiStrategyDTO, mulResultEnv, mulResultW, profitVsRate, finalProfitRate);
            }
        }

        return ResponseEntity.ok("Env strategy saved successfully");
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
    @GetMapping("/multi_result/**")
    public ResponseEntity<?> getMultiStrategyResult(@RequestHeader("Authorization") String token){
        Long userId = jwtUtil.getUserIdFromToken(token);
        MultiStrategyDTO multiStrategyDTO = strategyService.getLatestMultiStrategyResultByUserId(userId);
        return ResponseEntity.ok(multiStrategyDTO);
    }
}