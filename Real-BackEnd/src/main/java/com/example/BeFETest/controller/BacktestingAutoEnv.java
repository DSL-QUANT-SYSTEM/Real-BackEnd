package com.example.BeFETest.controller;

import com.example.BeFETest.DTO.coinDTO.EnvelopeDTO;
import com.example.BeFETest.DTO.coinDTO.StrategyCommonDTO;
import com.example.BeFETest.Strategy.BacktestingEnv;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Random;

public class BacktestingAutoEnv {

    // CSV 파일에 백테스트 결과 저장
    public static void logBacktestEnvResult(EnvelopeDTO env) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("backtesting_Env_results.csv", true))) {
            String result = String.format("\"%s\",\"%f\",\"%f\",\"%f\",\"%f\",\"%f\",\"%d\"",
                    env.getBacktesting_date(),
                    env.getFinalCash(),
                    env.getFinalAsset(),
                    env.getFinalBalance(),
                    env.getProfit(),
                    env.getProfitRate(),
                    env.getNumberOfTrades()
            );
            writer.write(result);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error writing backtest result: " + e.getMessage());
        }
    }

    // 랜덤한 전략 조건 생성
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

    // 자동 백테스팅 실행
    public static void runAutomaticBacktesting(int numberOfTests) {
        for (int i = 0; i < numberOfTests; i++) {
            // 1. 랜덤 전략 생성
            EnvelopeDTO strategy = generateRandomEnv();

            // 2. 공통 DTO 설정 (예시로 고정된 값)
            StrategyCommonDTO commonDTO = new StrategyCommonDTO(
                    strategy.getInitial_investment(),
                    strategy.getTax(),
                    strategy.getBacktesting_date(),
                    strategy.getTarget_item(),
                    strategy.getTick_kind(),
                    strategy.getInq_range(),
                    strategy.getStrategy(),
                    strategy.getFinalCash(),
                    strategy.getFinalAsset(),
                    strategy.getFinalBalance(),
                    strategy.getProfit(),
                    strategy.getProfitRate(),
                    strategy.getNumberOfTrades()
            );

            // 3. 백테스팅 실행
            EnvelopeDTO result = BacktestingEnv.executeTrades(commonDTO, strategy);

            // 4. 백테스트 결과 저장
            logBacktestEnvResult(result);
        }
    }

    public static void main(String[] args) {
        // 10번의 백테스팅을 자동으로 실행
        runAutomaticBacktesting(10);
    }
}

