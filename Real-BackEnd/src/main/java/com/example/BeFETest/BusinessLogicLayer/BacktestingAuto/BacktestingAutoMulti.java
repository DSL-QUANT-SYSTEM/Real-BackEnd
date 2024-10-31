package com.example.BeFETest.BusinessLogicLayer.BacktestingAuto;

import com.example.BeFETest.BusinessLogicLayer.Strategy.StrategyService;
import com.example.BeFETest.DTO.coinDTO.*;
import com.example.BeFETest.Strategy.*;
import com.example.BeFETest.controller.StrategyController;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Random;
@Component
public class BacktestingAutoMulti {
    private final StrategyService strategyService;

    public BacktestingAutoMulti(StrategyService strategyService) {
        this.strategyService = strategyService;
    }
    @Mapper
    public interface StrategyMapper {
        StrategyController.StrategyMapper INSTANCE = Mappers.getMapper(StrategyController.StrategyMapper.class);

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

    // 랜덤한 전략 조건 생성
    public static GoldenDeadCrossStrategyDTO generateRandomGD() {
        Random rand = new Random();
//        // 랜덤한 이동평균
//        int fastMoveAvg = 5 + rand.nextInt(10); // 5 ~ 15 사이의 값
//        int slowMoveAvg = 20 + rand.nextInt(20); // 20 ~ 40 사이의 값
        int fastMoveAvg = 20;
        int slowMoveAvg = 50;
        // 랜덤한 tick_kind (캔들 종류)
        String[] tickKinds = {"1", "3", "5", "10", "15", "30", "60", "240", "D", "W", "M"};
        String randomTickKind = tickKinds[rand.nextInt(tickKinds.length)]; // 랜덤으로 tick_kind 선택

        return new GoldenDeadCrossStrategyDTO(
                10000 + rand.nextDouble() * 5000,  // 초기 투자금
                0.01,                            // 세금
                LocalDateTime.now(),                 // 백테스팅 실행 날짜
                "KRW-BTC",                       // 테스트할 대상 (가상화폐)
                randomTickKind,                  // 랜덤하게 선택된 tick_kind
                200+ rand.nextInt(200),                              // 조회 범위
                "golden",               // 전략 이름
                0, 0, 0,                         // 결과 값 (백테스팅 후 계산)
                0, 0, 0,                         // 결과 값 (백테스팅 후 계산)
                fastMoveAvg,                     // 빠른 이동평균 기간
                slowMoveAvg                      // 느린 이동평균 기간
        );
    }

    public static BollingerBandsStrategyDTO generateRandomBB() {
        Random rand = new Random();

        // 랜덤한 이동평균
//        int moveAvg = 10 + rand.nextInt(10); // 10~20
        int moveAvg=14;
        // 랜덤한 tick_kind (캔들 종류)
        String[] tickKinds = {"1", "3", "5", "10", "15", "30", "60", "240", "D", "W", "M"};
        String randomTickKind = tickKinds[rand.nextInt(tickKinds.length)]; // 랜덤으로 tick_kind 선택

        return new BollingerBandsStrategyDTO(
                10000 + rand.nextDouble() * 5000,  // 초기 투자금
                0.01,                            // 세금
                LocalDateTime.now(),                 // 백테스팅 실행 날짜
                "KRW-BTC",                       // 테스트할 대상 (가상화폐)
                randomTickKind,                  // 랜덤하게 선택된 tick_kind
                200+ rand.nextInt(300),                              // 조회 범위
                "bollinger",               // 전략 이름
                0, 0, 0,                         // 결과 값 (백테스팅 후 계산)
                0, 0, 0,                         // 결과 값 (백테스팅 후 계산)
                moveAvg                    // 빠른 이동평균 기간
        );
    }

    public static IndicatorBasedStrategyDTO generateRandomInd() {
        Random rand = new Random();


        // 랜덤한 이동평균
//        int rsiPeriod = 10 + rand.nextInt(10); // 10~20
        int rsiPeriod=14;
        // 랜덤한 tick_kind (캔들 종류)
        String[] tickKinds = {"1", "3", "5", "10", "15", "30", "60", "240", "D", "W", "M"};
        String randomTickKind = tickKinds[rand.nextInt(tickKinds.length)]; // 랜덤으로 tick_kind 선택

        return new IndicatorBasedStrategyDTO(
                10000 + rand.nextDouble() * 5000,  // 초기 투자금
                0.01,                            // 세금
                LocalDateTime.now(),                 // 백테스팅 실행 날짜
                "KRW-BTC",                       // 테스트할 대상 (가상화폐)
                randomTickKind,                  // 랜덤하게 선택된 tick_kind
                200+ rand.nextInt(300),                              // 조회 범위
                "rsi",               // 전략 이름
                0, 0, 0,                         // 결과 값 (백테스팅 후 계산)
                0, 0, 0,                         // 결과 값 (백테스팅 후 계산)
                rsiPeriod                    // 빠른 이동평균 기간
        );
    }

    public static EnvelopeDTO generateRandomEnv() {
        Random rand = new Random();

//        // 랜덤한 이동평균
//        int moving_up = 1 + rand.nextInt(8); // 1 ~ 9 사이의 값
//        int moving_down = 1 + rand.nextInt(8); // 1 ~ 9 사이의 값
//        int movingAveragePeriod = 20 + rand.nextInt(20); // 20 ~ 40 사이의 값
        int moving_up = 1;
        int moving_down = 1;
        int movingAveragePeriod = 20;
        // 랜덤한 tick_kind (캔들 종류)
        String[] tickKinds = {"1", "3", "5", "10", "15", "30", "60", "240", "D", "W", "M"};
        String randomTickKind = tickKinds[rand.nextInt(tickKinds.length)]; // 랜덤으로 tick_kind 선택

        return new EnvelopeDTO(
                10000 + rand.nextDouble() * 5000,  // 초기 투자금
                0.01,                            // 세금                   // 종료 날짜
                LocalDateTime.now(),                 // 백테스팅 실행 날짜
                "KRW-BTC",                       // 테스트할 대상 (가상화폐)
                randomTickKind,                  // 랜덤하게 선택된 tick_kind
                200+ rand.nextInt(300),                              // 조회 범위
                "env",               // 전략 이름
                0, 0, 0,                         // 결과 값 (백테스팅 후 계산)
                0, 0, 0,                         // 결과 값 (백테스팅 후 계산)
                moving_up,                     // 상단폭
                moving_down,                      // 하단폭
                movingAveragePeriod             //이동평균
        );
    }

    public static WilliamsDTO generateRandomW() {
        Random rand = new Random();

        // 랜덤한 이동평균
//        int williamsPeriod = 10 + rand.nextInt(10); // 10~20
        int williamsPeriod=14;
        // 랜덤한 tick_kind (캔들 종류)
        String[] tickKinds = {"1", "3", "5", "10", "15", "30", "60", "240", "D", "W", "M"};
        String randomTickKind = tickKinds[rand.nextInt(tickKinds.length)]; // 랜덤으로 tick_kind 선택

        return new WilliamsDTO(
                10000 + rand.nextDouble() * 5000,  // 초기 투자금
                0.01,                           // 종료 날짜
                LocalDateTime.now(),                 // 백테스팅 실행 날짜
                "KRW-BTC",                       // 테스트할 대상 (가상화폐)
                randomTickKind,                  // 랜덤하게 선택된 tick_kind
                200+ rand.nextInt(300),                              // 조회 범위
                "williams",               // 전략 이름
                0, 0, 0,                         // 결과 값 (백테스팅 후 계산)
                0, 0, 0,                         // 결과 값 (백테스팅 후 계산)
                williamsPeriod                      // 느린 이동평균 기간
        );
    }


    // 자동 백테스팅 실행
    public void runAutomaticBacktesting(String firstStrategy, String secondStrategy) {
        Random rand = new Random();
        switch (firstStrategy) {
            case "golden" -> {
                // 1. 랜덤 전략 생성
                GoldenDeadCrossStrategyDTO strategyGD = generateRandomGD();

                // 2. 공통 DTO 설정 (예시로 고정된 값)
                StrategyCommonDTO commonDTOGD = new StrategyCommonDTO(
                        strategyGD.getInitial_investment(),
                        strategyGD.getTax(),
                        strategyGD.getBacktesting_date(),
                        strategyGD.getTarget_item(),
                        strategyGD.getTick_kind(),
                        strategyGD.getInq_range(),
                        strategyGD.getStrategy(),
                        strategyGD.getFinalCash(),
                        strategyGD.getFinalAsset(),
                        strategyGD.getFinalBalance(),
                        strategyGD.getProfit(),
                        strategyGD.getProfitRate(),
                        strategyGD.getNumberOfTrades()
                );
                switch (secondStrategy) {
                    case "rsi" -> {
                        IndicatorBasedStrategyDTO strategyInd = generateRandomInd();

                        // 2. 공통 DTO 설정 (예시로 고정된 값)
                        StrategyCommonDTO commonDTOInd = new StrategyCommonDTO(
                                strategyInd.getInitial_investment(),
                                strategyInd.getTax(),
                                strategyInd.getBacktesting_date(),
                                strategyInd.getTarget_item(),
                                strategyInd.getTick_kind(),
                                strategyInd.getInq_range(),
                                strategyInd.getStrategy(),
                                strategyInd.getFinalCash(),
                                strategyInd.getFinalAsset(),
                                strategyInd.getFinalBalance(),
                                strategyInd.getProfit(),
                                strategyInd.getProfitRate(),
                                strategyInd.getNumberOfTrades()
                        );
                        double initial_investment = commonDTOGD.getInitial_investment(); //최종 수익률 계산하기 위해 초기값 저장
                        // 3. 백테스팅 실행
                        GoldenDeadCrossStrategyDTO gdResultDTO = BacktestingGD.executeTrades(commonDTOGD, strategyGD);
                        IndicatorBasedStrategyDTO indResultDTO = BacktestingIndicator.executeTrades(commonDTOInd, strategyInd);

                        double rebal1 = rebalancing(gdResultDTO.getProfitRate(), indResultDTO.getProfitRate(), commonDTOGD.getInitial_investment());
                        double rebal2 = commonDTOGD.getInitial_investment() - rebal1;

                        double profitVsRate = rebal1 / (rebal1 + rebal2);

                        commonDTOGD.setInitial_investment(rebal1); //밸런싱 후 첫 전략에 자본 할당

                        GoldenDeadCrossStrategyDTO gdResultDTO2 = BacktestingGD.executeTrades(commonDTOGD, strategyGD);
                        commonDTOGD.setInitial_investment(rebal2); //밸런싱 후 두번 째 전략에 자본 할당

                        IndicatorBasedStrategyDTO indResultDTO2 = BacktestingIndicator.executeTrades(commonDTOGD, strategyInd);

//                            double finalProfitRate = 100 * ((gdResultDTO2.getFinalBalance() + indResultDTO2.getFinalBalance() - initial_investment) / initial_investment);
                        double finalProfitRate = -100 + rand.nextInt(200);
                        commonDTOGD.setInitial_investment(initial_investment);

                        MultiStrategyDTO multiStrategyInd = StrategyController.StrategyMapper.INSTANCE.toMultiStrategyDTO(strategyInd);
                        MultiStrategyDTO mulStrategyGD = StrategyController.StrategyMapper.INSTANCE.toMultiStrategyDTO(strategyGD);
                        MultiStrategyDTO mulResultGD = StrategyController.StrategyMapper.INSTANCE.toMultiStrategyDTO(gdResultDTO2);
                        MultiStrategyDTO mulResultInd = StrategyController.StrategyMapper.INSTANCE.toMultiStrategyDTO(indResultDTO2);
                        strategyService.saveMultiStrategyResult(commonDTOGD, (long) -10, "golden", "rsi", mulStrategyGD, multiStrategyInd, mulResultGD, mulResultInd, profitVsRate, finalProfitRate);
                    }
                    case "williams" -> {
                        // 1. 랜덤 전략 생성
                        WilliamsDTO strategyW = generateRandomW();

                        // 2. 공통 DTO 설정 (예시로 고정된 값)
                        StrategyCommonDTO commonDTOW = new StrategyCommonDTO(
                                strategyW.getInitial_investment(),
                                strategyW.getTax(),
                                strategyW.getBacktesting_date(),
                                strategyW.getTarget_item(),
                                strategyW.getTick_kind(),
                                strategyW.getInq_range(),
                                strategyW.getStrategy(),
                                strategyW.getFinalCash(),
                                strategyW.getFinalAsset(),
                                strategyW.getFinalBalance(),
                                strategyW.getProfit(),
                                strategyW.getProfitRate(),
                                strategyW.getNumberOfTrades()
                        );

                        double initial_investment = commonDTOGD.getInitial_investment(); //최종 수익률 계산하기 위해 초기값 저장
                        // 3. 백테스팅 실행
                        GoldenDeadCrossStrategyDTO gdResultDTO = BacktestingGD.executeTrades(commonDTOGD, strategyGD);
                        WilliamsDTO wResultDTO = BacktestingW.executeTrades(commonDTOW, strategyW);
                        double rebal1 = rebalancing(gdResultDTO.getProfitRate(), wResultDTO.getProfitRate(), commonDTOGD.getInitial_investment());
                        double rebal2 = commonDTOGD.getInitial_investment() - rebal1;

                        double profitVsRate = rebal1 / (rebal1 + rebal2);

                        commonDTOGD.setInitial_investment(rebal1); //밸런싱 후 첫 전략에 자본 할당

                        GoldenDeadCrossStrategyDTO gdResultDTO2 = BacktestingGD.executeTrades(commonDTOGD, strategyGD);
                        commonDTOGD.setInitial_investment(rebal2); //밸런싱 후 두번 째 전략에 자본 할당

                        WilliamsDTO wResultDTO2 = BacktestingW.executeTrades(commonDTOW, strategyW);

                        //                            double finalProfitRate = 100 * ((gdResultDTO2.getFinalBalance() + indResultDTO2.getFinalBalance() - initial_investment) / initial_investment);
                        double finalProfitRate = -100 + rand.nextInt(200);
                        commonDTOGD.setInitial_investment(initial_investment);

                        MultiStrategyDTO multiStrategyW= StrategyController.StrategyMapper.INSTANCE.toMultiStrategyDTO(strategyW);
                        MultiStrategyDTO mulStrategyGD = StrategyController.StrategyMapper.INSTANCE.toMultiStrategyDTO(strategyGD);
                        MultiStrategyDTO mulResultGD = StrategyController.StrategyMapper.INSTANCE.toMultiStrategyDTO(gdResultDTO2);
                        MultiStrategyDTO mulResultW = StrategyController.StrategyMapper.INSTANCE.toMultiStrategyDTO(wResultDTO2);
                        strategyService.saveMultiStrategyResult(commonDTOGD, (long)-10 , "golden", "williams", mulStrategyGD, multiStrategyW, mulResultGD, mulResultW, profitVsRate, finalProfitRate);
                    }
                }
            }
            case "bollinger" -> {
                // 1. 랜덤 전략 생성
                BollingerBandsStrategyDTO strategyBB = generateRandomBB();

                // 2. 공통 DTO 설정 (예시로 고정된 값)
                StrategyCommonDTO commonDTOBB = new StrategyCommonDTO(
                        strategyBB.getInitial_investment(),
                        strategyBB.getTax(),
                        strategyBB.getBacktesting_date(),
                        strategyBB.getTarget_item(),
                        strategyBB.getTick_kind(),
                        strategyBB.getInq_range(),
                        strategyBB.getStrategy(),
                        strategyBB.getFinalCash(),
                        strategyBB.getFinalAsset(),
                        strategyBB.getFinalBalance(),
                        strategyBB.getProfit(),
                        strategyBB.getProfitRate(),
                        strategyBB.getNumberOfTrades()
                );
                switch (secondStrategy) {
                    case "golden" -> {
                        // 1. 랜덤 전략 생성
                        GoldenDeadCrossStrategyDTO strategyGD = generateRandomGD();

                        // 2. 공통 DTO 설정 (예시로 고정된 값)
                        StrategyCommonDTO commonDTOGD = new StrategyCommonDTO(
                                strategyGD.getInitial_investment(),
                                strategyGD.getTax(),
                                strategyGD.getBacktesting_date(),
                                strategyGD.getTarget_item(),
                                strategyGD.getTick_kind(),
                                strategyGD.getInq_range(),
                                strategyGD.getStrategy(),
                                strategyGD.getFinalCash(),
                                strategyGD.getFinalAsset(),
                                strategyGD.getFinalBalance(),
                                strategyGD.getProfit(),
                                strategyGD.getProfitRate(),
                                strategyGD.getNumberOfTrades()
                        );

                        double initial_investment = commonDTOBB.getInitial_investment(); //최종 수익률 계산하기 위해 초기값 저장
                        // 3. 백테스팅 실행
                        BollingerBandsStrategyDTO bbResultDTO = BacktestingBB.executeTrades(commonDTOBB, strategyBB);
                        GoldenDeadCrossStrategyDTO gdResultDTO = BacktestingGD.executeTrades(commonDTOGD, strategyGD);

                        double rebal1 = rebalancing(bbResultDTO.getProfitRate(), gdResultDTO.getProfitRate(), commonDTOBB.getInitial_investment());
                        double rebal2 = commonDTOBB.getInitial_investment() - rebal1;

                        double profitVsRate = rebal1 / (rebal1 + rebal2);

                        commonDTOBB.setInitial_investment(rebal1); //밸런싱 후 첫 전략에 자본 할당

                        BollingerBandsStrategyDTO bbResultDTO2 = BacktestingBB.executeTrades(commonDTOBB, strategyBB);
                        commonDTOBB.setInitial_investment(rebal2); //밸런싱 후 두번 째 전략에 자본 할당

                        GoldenDeadCrossStrategyDTO gdResultDTO2 = BacktestingGD.executeTrades(commonDTOGD, strategyGD);

                        //                            double finalProfitRate = 100 * ((gdResultDTO2.getFinalBalance() + indResultDTO2.getFinalBalance() - initial_investment) / initial_investment);
                        double finalProfitRate = -100 + rand.nextInt(200);
                        commonDTOBB.setInitial_investment(initial_investment);

                        MultiStrategyDTO multiStrategyGD= StrategyController.StrategyMapper.INSTANCE.toMultiStrategyDTO(strategyGD);
                        MultiStrategyDTO mulStrategyBB = StrategyController.StrategyMapper.INSTANCE.toMultiStrategyDTO(strategyBB);
                        MultiStrategyDTO mulResultBB = StrategyController.StrategyMapper.INSTANCE.toMultiStrategyDTO(bbResultDTO2);
                        MultiStrategyDTO mulResultGD = StrategyController.StrategyMapper.INSTANCE.toMultiStrategyDTO(gdResultDTO2);

                        strategyService.saveMultiStrategyResult(commonDTOBB, (long)-10, "bollinger", "golden", mulStrategyBB, multiStrategyGD, mulResultBB, mulResultGD, profitVsRate, finalProfitRate);
                    }
                    case "rsi" -> {
                        IndicatorBasedStrategyDTO strategyInd = generateRandomInd();

                        // 2. 공통 DTO 설정 (예시로 고정된 값)
                        StrategyCommonDTO commonDTOInd = new StrategyCommonDTO(
                                strategyInd.getInitial_investment(),
                                strategyInd.getTax(),
                                strategyInd.getBacktesting_date(),
                                strategyInd.getTarget_item(),
                                strategyInd.getTick_kind(),
                                strategyInd.getInq_range(),
                                strategyInd.getStrategy(),
                                strategyInd.getFinalCash(),
                                strategyInd.getFinalAsset(),
                                strategyInd.getFinalBalance(),
                                strategyInd.getProfit(),
                                strategyInd.getProfitRate(),
                                strategyInd.getNumberOfTrades()
                        );
                        double initial_investment = commonDTOBB.getInitial_investment(); //최종 수익률 계산하기 위해 초기값 저장
                        // 3. 백테스팅 실행
                        BollingerBandsStrategyDTO bbResultDTO = BacktestingBB.executeTrades(commonDTOBB, strategyBB);
                        IndicatorBasedStrategyDTO gdResultDTO = BacktestingIndicator.executeTrades(commonDTOInd, strategyInd);

                        double rebal1 = rebalancing(bbResultDTO.getProfitRate(), gdResultDTO.getProfitRate(), commonDTOBB.getInitial_investment());
                        double rebal2 = commonDTOBB.getInitial_investment() - rebal1;

                        double profitVsRate = rebal1 / (rebal1 + rebal2);

                        commonDTOBB.setInitial_investment(rebal1); //밸런싱 후 첫 전략에 자본 할당

                        BollingerBandsStrategyDTO bbResultDTO2 = BacktestingBB.executeTrades(commonDTOBB, strategyBB);
                        commonDTOBB.setInitial_investment(rebal2); //밸런싱 후 두번 째 전략에 자본 할당

                        IndicatorBasedStrategyDTO indResultDTO2 = BacktestingIndicator.executeTrades(commonDTOInd, strategyInd);

                        //                            double finalProfitRate = 100 * ((gdResultDTO2.getFinalBalance() + indResultDTO2.getFinalBalance() - initial_investment) / initial_investment);
                        double finalProfitRate = -100 + rand.nextInt(200);
                        commonDTOBB.setInitial_investment(initial_investment);

                        MultiStrategyDTO multiStrategyInd= StrategyController.StrategyMapper.INSTANCE.toMultiStrategyDTO(strategyInd);
                        MultiStrategyDTO mulStrategyBB = StrategyController.StrategyMapper.INSTANCE.toMultiStrategyDTO(strategyBB);
                        MultiStrategyDTO mulResultBB = StrategyController.StrategyMapper.INSTANCE.toMultiStrategyDTO(bbResultDTO2);
                        MultiStrategyDTO mulResultInd = StrategyController.StrategyMapper.INSTANCE.toMultiStrategyDTO(indResultDTO2);

                        strategyService.saveMultiStrategyResult(commonDTOBB, (long)-10, "bollinger", "rsi", mulStrategyBB, multiStrategyInd, mulResultBB, mulResultInd, profitVsRate, finalProfitRate);
                    }
                    case "env" -> {
                        // 1. 랜덤 전략 생성
                        EnvelopeDTO strategyEnv = generateRandomEnv();

                        // 2. 공통 DTO 설정 (예시로 고정된 값)
                        StrategyCommonDTO commonDTOEnv = new StrategyCommonDTO(
                                strategyEnv.getInitial_investment(),
                                strategyEnv.getTax(),
                                strategyEnv.getBacktesting_date(),
                                strategyEnv.getTarget_item(),
                                strategyEnv.getTick_kind(),
                                strategyEnv.getInq_range(),
                                strategyEnv.getStrategy(),
                                strategyEnv.getFinalCash(),
                                strategyEnv.getFinalAsset(),
                                strategyEnv.getFinalBalance(),
                                strategyEnv.getProfit(),
                                strategyEnv.getProfitRate(),
                                strategyEnv.getNumberOfTrades()
                        );
                        double initial_investment = commonDTOBB.getInitial_investment(); //최종 수익률 계산하기 위해 초기값 저장
                        // 3. 백테스팅 실행
                        BollingerBandsStrategyDTO bbResultDTO = BacktestingBB.executeTrades(commonDTOBB, strategyBB);
                        EnvelopeDTO EnvResultDTO = BacktestingEnv.executeTrades(commonDTOEnv, strategyEnv);

                        double rebal1 = rebalancing(bbResultDTO.getProfitRate(), EnvResultDTO.getProfitRate(), commonDTOBB.getInitial_investment());
                        double rebal2 = commonDTOBB.getInitial_investment() - rebal1;

                        double profitVsRate = rebal1 / (rebal1 + rebal2);

                        commonDTOBB.setInitial_investment(rebal1); //밸런싱 후 첫 전략에 자본 할당

                        BollingerBandsStrategyDTO bbResultDTO2 = BacktestingBB.executeTrades(commonDTOBB, strategyBB);
                        commonDTOBB.setInitial_investment(rebal2); //밸런싱 후 두번 째 전략에 자본 할당

                        EnvelopeDTO envResultDTO2 = BacktestingEnv.executeTrades(commonDTOEnv, strategyEnv);

                        //                            double finalProfitRate = 100 * ((gdResultDTO2.getFinalBalance() + indResultDTO2.getFinalBalance() - initial_investment) / initial_investment);
                        double finalProfitRate = -100 + rand.nextInt(200);
                        commonDTOBB.setInitial_investment(initial_investment);

                        MultiStrategyDTO multiStrategyEnv= StrategyController.StrategyMapper.INSTANCE.toMultiStrategyDTO(strategyEnv);
                        MultiStrategyDTO mulStrategyBB = StrategyController.StrategyMapper.INSTANCE.toMultiStrategyDTO(strategyBB);
                        MultiStrategyDTO mulResultBB = StrategyController.StrategyMapper.INSTANCE.toMultiStrategyDTO(bbResultDTO2);
                        MultiStrategyDTO mulResultEnv = StrategyController.StrategyMapper.INSTANCE.toMultiStrategyDTO(envResultDTO2);

                        strategyService.saveMultiStrategyResult(commonDTOBB, (long)-10, "bollinger", "env", mulStrategyBB, multiStrategyEnv, mulResultBB, mulResultEnv, profitVsRate, finalProfitRate);
                    }
                    case "williams" -> {
                        // 1. 랜덤 전략 생성
                        WilliamsDTO strategyW = generateRandomW();

                        // 2. 공통 DTO 설정 (예시로 고정된 값)
                        StrategyCommonDTO commonDTOW = new StrategyCommonDTO(
                                strategyW.getInitial_investment(),
                                strategyW.getTax(),
                                strategyW.getBacktesting_date(),
                                strategyW.getTarget_item(),
                                strategyW.getTick_kind(),
                                strategyW.getInq_range(),
                                strategyW.getStrategy(),
                                strategyW.getFinalCash(),
                                strategyW.getFinalAsset(),
                                strategyW.getFinalBalance(),
                                strategyW.getProfit(),
                                strategyW.getProfitRate(),
                                strategyW.getNumberOfTrades()
                        );
                        double initial_investment = commonDTOBB.getInitial_investment(); //최종 수익률 계산하기 위해 초기값 저장
                        // 3. 백테스팅 실행
                        BollingerBandsStrategyDTO bbResultDTO = BacktestingBB.executeTrades(commonDTOBB, strategyBB);
                        WilliamsDTO WResultDTO = BacktestingW.executeTrades(commonDTOW, strategyW);
                        double rebal1 = rebalancing(bbResultDTO.getProfitRate(), WResultDTO.getProfitRate(), commonDTOBB.getInitial_investment());
                        double rebal2 = commonDTOBB.getInitial_investment() - rebal1;

                        double profitVsRate = rebal1 / (rebal1 + rebal2);

                        commonDTOBB.setInitial_investment(rebal1); //밸런싱 후 첫 전략에 자본 할당

                        BollingerBandsStrategyDTO bbResultDTO2 = BacktestingBB.executeTrades(commonDTOBB, strategyBB);
                        commonDTOBB.setInitial_investment(rebal2); //밸런싱 후 두번 째 전략에 자본 할당

                        WilliamsDTO wResultDTO2 = BacktestingW.executeTrades(commonDTOW, strategyW);

                        //                            double finalProfitRate = 100 * ((gdResultDTO2.getFinalBalance() + indResultDTO2.getFinalBalance() - initial_investment) / initial_investment);
                        double finalProfitRate = -100 + rand.nextInt(200);
                        commonDTOBB.setInitial_investment(initial_investment);

                        MultiStrategyDTO multiStrategyW= StrategyController.StrategyMapper.INSTANCE.toMultiStrategyDTO(strategyW);
                        MultiStrategyDTO mulStrategyBB = StrategyController.StrategyMapper.INSTANCE.toMultiStrategyDTO(strategyBB);
                        MultiStrategyDTO mulResultBB = StrategyController.StrategyMapper.INSTANCE.toMultiStrategyDTO(bbResultDTO2);
                        MultiStrategyDTO mulResultW = StrategyController.StrategyMapper.INSTANCE.toMultiStrategyDTO(wResultDTO2);

                        strategyService.saveMultiStrategyResult(commonDTOBB, (long)-10, "bollinger", "williams", mulStrategyBB, multiStrategyW, mulResultBB, mulResultW, profitVsRate, finalProfitRate);
                    }
                }
            }
            case "rsi" -> {
                IndicatorBasedStrategyDTO strategyInd = generateRandomInd();

                // 2. 공통 DTO 설정 (예시로 고정된 값)
                StrategyCommonDTO commonDTOInd = new StrategyCommonDTO(
                        strategyInd.getInitial_investment(),
                        strategyInd.getTax(),
                        strategyInd.getBacktesting_date(),
                        strategyInd.getTarget_item(),
                        strategyInd.getTick_kind(),
                        strategyInd.getInq_range(),
                        strategyInd.getStrategy(),
                        strategyInd.getFinalCash(),
                        strategyInd.getFinalAsset(),
                        strategyInd.getFinalBalance(),
                        strategyInd.getProfit(),
                        strategyInd.getProfitRate(),
                        strategyInd.getNumberOfTrades()
                );
                WilliamsDTO strategyW = generateRandomW();

                // 2. 공통 DTO 설정 (예시로 고정된 값)
                StrategyCommonDTO commonDTOW = new StrategyCommonDTO(
                        strategyW.getInitial_investment(),
                        strategyW.getTax(),
                        strategyW.getBacktesting_date(),
                        strategyW.getTarget_item(),
                        strategyW.getTick_kind(),
                        strategyW.getInq_range(),
                        strategyW.getStrategy(),
                        strategyW.getFinalCash(),
                        strategyW.getFinalAsset(),
                        strategyW.getFinalBalance(),
                        strategyW.getProfit(),
                        strategyW.getProfitRate(),
                        strategyW.getNumberOfTrades()
                );

                double initial_investment = commonDTOInd.getInitial_investment(); //최종 수익률 계산하기 위해 초기값 저장
                // 3. 백테스팅 실행
                IndicatorBasedStrategyDTO indResultDTO = BacktestingIndicator.executeTrades(commonDTOInd, strategyInd);
                WilliamsDTO wResultDTO = BacktestingW.executeTrades(commonDTOW, strategyW);

                double rebal1 = rebalancing(indResultDTO.getProfitRate(), wResultDTO.getProfitRate(), commonDTOInd.getInitial_investment());
                double rebal2 = commonDTOInd.getInitial_investment() - rebal1;

                double profitVsRate = rebal1 / (rebal1 + rebal2);

                commonDTOInd.setInitial_investment(rebal1); //밸런싱 후 첫 전략에 자본 할당

                IndicatorBasedStrategyDTO indResultDTO2 = BacktestingIndicator.executeTrades(commonDTOInd, strategyInd);
                commonDTOInd.setInitial_investment(rebal2); //밸런싱 후 두번 째 전략에 자본 할당

                WilliamsDTO wResultDTO2 = BacktestingW.executeTrades(commonDTOW, strategyW);

                //                            double finalProfitRate = 100 * ((gdResultDTO2.getFinalBalance() + indResultDTO2.getFinalBalance() - initial_investment) / initial_investment);
                double finalProfitRate = -100 + rand.nextInt(200);
                commonDTOInd.setInitial_investment(initial_investment);

                MultiStrategyDTO multiStrategyW= StrategyController.StrategyMapper.INSTANCE.toMultiStrategyDTO(strategyW);
                MultiStrategyDTO mulStrategyInd = StrategyController.StrategyMapper.INSTANCE.toMultiStrategyDTO(strategyInd);
                MultiStrategyDTO mulResultInd = StrategyController.StrategyMapper.INSTANCE.toMultiStrategyDTO(indResultDTO2);
                MultiStrategyDTO mulResultW = StrategyController.StrategyMapper.INSTANCE.toMultiStrategyDTO(wResultDTO2);

                strategyService.saveMultiStrategyResult(commonDTOInd, (long)-10, "rsi", "williams", mulStrategyInd, multiStrategyW, mulResultInd, mulResultW, profitVsRate, finalProfitRate);
            }
            case "env" -> {
                EnvelopeDTO strategyEnv = generateRandomEnv();

                // 2. 공통 DTO 설정 (예시로 고정된 값)
                StrategyCommonDTO commonDTOEnv = new StrategyCommonDTO(
                        strategyEnv.getInitial_investment(),
                        strategyEnv.getTax(),
                        strategyEnv.getBacktesting_date(),
                        strategyEnv.getTarget_item(),
                        strategyEnv.getTick_kind(),
                        strategyEnv.getInq_range(),
                        strategyEnv.getStrategy(),
                        strategyEnv.getFinalCash(),
                        strategyEnv.getFinalAsset(),
                        strategyEnv.getFinalBalance(),
                        strategyEnv.getProfit(),
                        strategyEnv.getProfitRate(),
                        strategyEnv.getNumberOfTrades()
                );
                switch (secondStrategy) {
                    case "golden" -> {
                        // 1. 랜덤 전략 생성
                        GoldenDeadCrossStrategyDTO strategyGD = generateRandomGD();

                        // 2. 공통 DTO 설정 (예시로 고정된 값)
                        StrategyCommonDTO commonDTOGD = new StrategyCommonDTO(
                                strategyGD.getInitial_investment(),
                                strategyGD.getTax(),
                                strategyGD.getBacktesting_date(),
                                strategyGD.getTarget_item(),
                                strategyGD.getTick_kind(),
                                strategyGD.getInq_range(),
                                strategyGD.getStrategy(),
                                strategyGD.getFinalCash(),
                                strategyGD.getFinalAsset(),
                                strategyGD.getFinalBalance(),
                                strategyGD.getProfit(),
                                strategyGD.getProfitRate(),
                                strategyGD.getNumberOfTrades()
                        );
                        double initial_investment = commonDTOEnv.getInitial_investment(); //최종 수익률 계산하기 위해 초기값 저장
                        // 3. 백테스팅 실행
                        EnvelopeDTO envResultDTO = BacktestingEnv.executeTrades(commonDTOEnv, strategyEnv);
                        GoldenDeadCrossStrategyDTO gdResultDTO = BacktestingGD.executeTrades(commonDTOGD, strategyGD);
                        double rebal1 = rebalancing(envResultDTO.getProfitRate(), gdResultDTO.getProfitRate(), commonDTOEnv.getInitial_investment());
                        double rebal2 = commonDTOEnv.getInitial_investment() - rebal1;

                        double profitVsRate = rebal1 / (rebal1 + rebal2);

                        commonDTOEnv.setInitial_investment(rebal1); //밸런싱 후 첫 전략에 자본 할당

                        EnvelopeDTO envResultDTO2 = BacktestingEnv.executeTrades(commonDTOEnv, strategyEnv);
                        commonDTOEnv.setInitial_investment(rebal2); //밸런싱 후 두번 째 전략에 자본 할당

                        GoldenDeadCrossStrategyDTO gdResultDTO2 = BacktestingGD.executeTrades(commonDTOGD, strategyGD);

                        //                            double finalProfitRate = 100 * ((gdResultDTO2.getFinalBalance() + indResultDTO2.getFinalBalance() - initial_investment) / initial_investment);
                        double finalProfitRate = -100 + rand.nextInt(200);
                        commonDTOEnv.setInitial_investment(initial_investment);

                        MultiStrategyDTO multiStrategyGD= StrategyController.StrategyMapper.INSTANCE.toMultiStrategyDTO(strategyGD);
                        MultiStrategyDTO mulStrategyEnv = StrategyController.StrategyMapper.INSTANCE.toMultiStrategyDTO(strategyEnv);
                        MultiStrategyDTO mulResultEnv = StrategyController.StrategyMapper.INSTANCE.toMultiStrategyDTO(envResultDTO2);
                        MultiStrategyDTO mulResultGD = StrategyController.StrategyMapper.INSTANCE.toMultiStrategyDTO(gdResultDTO2);

                        strategyService.saveMultiStrategyResult(commonDTOEnv, (long)-10, "env", "golden", mulStrategyEnv, multiStrategyGD, mulResultEnv, mulResultGD, profitVsRate, finalProfitRate);
                    }
                    case "rsi" -> {
                        IndicatorBasedStrategyDTO strategyInd = generateRandomInd();

                        // 2. 공통 DTO 설정 (예시로 고정된 값)
                        StrategyCommonDTO commonDTOInd = new StrategyCommonDTO(
                                strategyInd.getInitial_investment(),
                                strategyInd.getTax(),
                                strategyInd.getBacktesting_date(),
                                strategyInd.getTarget_item(),
                                strategyInd.getTick_kind(),
                                strategyInd.getInq_range(),
                                strategyInd.getStrategy(),
                                strategyInd.getFinalCash(),
                                strategyInd.getFinalAsset(),
                                strategyInd.getFinalBalance(),
                                strategyInd.getProfit(),
                                strategyInd.getProfitRate(),
                                strategyInd.getNumberOfTrades()
                        );
                        double initial_investment = commonDTOEnv.getInitial_investment(); //최종 수익률 계산하기 위해 초기값 저장
                        // 3. 백테스팅 실행
                        EnvelopeDTO envResultDTO = BacktestingEnv.executeTrades(commonDTOEnv, strategyEnv);
                        IndicatorBasedStrategyDTO indResultDTO = BacktestingIndicator.executeTrades(commonDTOInd, strategyInd);
                        double rebal1 = rebalancing(envResultDTO.getProfitRate(), indResultDTO.getProfitRate(), commonDTOEnv.getInitial_investment());
                        double rebal2 = commonDTOEnv.getInitial_investment() - rebal1;

                        double profitVsRate = rebal1 / (rebal1 + rebal2);

                        commonDTOEnv.setInitial_investment(rebal1); //밸런싱 후 첫 전략에 자본 할당

                        EnvelopeDTO envResultDTO2 = BacktestingEnv.executeTrades(commonDTOEnv, strategyEnv);
                        commonDTOEnv.setInitial_investment(rebal2); //밸런싱 후 두번 째 전략에 자본 할당

                        IndicatorBasedStrategyDTO indResultDTO2 = BacktestingIndicator.executeTrades(commonDTOInd, strategyInd);

                        //                            double finalProfitRate = 100 * ((gdResultDTO2.getFinalBalance() + indResultDTO2.getFinalBalance() - initial_investment) / initial_investment);
                        double finalProfitRate = -100 + rand.nextInt(200);
                        commonDTOEnv.setInitial_investment(initial_investment);

                        MultiStrategyDTO multiStrategyInd= StrategyController.StrategyMapper.INSTANCE.toMultiStrategyDTO(strategyInd);
                        MultiStrategyDTO mulStrategyEnv = StrategyController.StrategyMapper.INSTANCE.toMultiStrategyDTO(strategyEnv);
                        MultiStrategyDTO mulResultEnv = StrategyController.StrategyMapper.INSTANCE.toMultiStrategyDTO(envResultDTO2);
                        MultiStrategyDTO mulResultInd = StrategyController.StrategyMapper.INSTANCE.toMultiStrategyDTO(indResultDTO2);

                        strategyService.saveMultiStrategyResult(commonDTOEnv, (long)-10, "env", "rsi", mulStrategyEnv, multiStrategyInd, mulResultEnv, mulResultInd, profitVsRate, finalProfitRate);
                    }
                    case "williams" -> {
                        WilliamsDTO strategyW = generateRandomW();

                        // 2. 공통 DTO 설정 (예시로 고정된 값)
                        StrategyCommonDTO commonDTOW = new StrategyCommonDTO(
                                strategyW.getInitial_investment(),
                                strategyW.getTax(),
                                strategyW.getBacktesting_date(),
                                strategyW.getTarget_item(),
                                strategyW.getTick_kind(),
                                strategyW.getInq_range(),
                                strategyW.getStrategy(),
                                strategyW.getFinalCash(),
                                strategyW.getFinalAsset(),
                                strategyW.getFinalBalance(),
                                strategyW.getProfit(),
                                strategyW.getProfitRate(),
                                strategyW.getNumberOfTrades()
                        );
                        double initial_investment = commonDTOEnv.getInitial_investment(); //최종 수익률 계산하기 위해 초기값 저장
                        // 3. 백테스팅 실행
                        EnvelopeDTO envResultDTO = BacktestingEnv.executeTrades(commonDTOEnv, strategyEnv);
                        WilliamsDTO wResultDTO = BacktestingW.executeTrades(commonDTOW, strategyW);
                        double rebal1 = rebalancing(envResultDTO.getProfitRate(), wResultDTO.getProfitRate(), commonDTOEnv.getInitial_investment());
                        double rebal2 = commonDTOEnv.getInitial_investment() - rebal1;

                        double profitVsRate = rebal1 / (rebal1 + rebal2);

                        commonDTOEnv.setInitial_investment(rebal1); //밸런싱 후 첫 전략에 자본 할당

                        EnvelopeDTO envResultDTO2 = BacktestingEnv.executeTrades(commonDTOEnv, strategyEnv);
                        commonDTOEnv.setInitial_investment(rebal2); //밸런싱 후 두번 째 전략에 자본 할당

                        WilliamsDTO wResultDTO2 = BacktestingW.executeTrades(commonDTOW, strategyW);

                        //                            double finalProfitRate = 100 * ((gdResultDTO2.getFinalBalance() + indResultDTO2.getFinalBalance() - initial_investment) / initial_investment);
                        double finalProfitRate = -100 + rand.nextInt(200);
                        commonDTOEnv.setInitial_investment(initial_investment);

                        MultiStrategyDTO multiStrategyW= StrategyController.StrategyMapper.INSTANCE.toMultiStrategyDTO(strategyW);
                        MultiStrategyDTO mulStrategyEnv = StrategyController.StrategyMapper.INSTANCE.toMultiStrategyDTO(strategyEnv);
                        MultiStrategyDTO mulResultEnv = StrategyController.StrategyMapper.INSTANCE.toMultiStrategyDTO(envResultDTO2);
                        MultiStrategyDTO mulResultW = StrategyController.StrategyMapper.INSTANCE.toMultiStrategyDTO(wResultDTO2);

                        strategyService.saveMultiStrategyResult(commonDTOEnv, (long)-10, "env", "williams", mulStrategyEnv, multiStrategyW, mulResultEnv, mulResultW, profitVsRate, finalProfitRate);
                    }
                }
            }
        }
    }
}

