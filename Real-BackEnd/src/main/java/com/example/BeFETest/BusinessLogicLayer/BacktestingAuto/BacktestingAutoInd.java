package com.example.BeFETest.BusinessLogicLayer.BacktestingAuto;

import com.example.BeFETest.BusinessLogicLayer.Strategy.StrategyService;
import com.example.BeFETest.DTO.coinDTO.IndicatorBasedStrategyDTO;
import com.example.BeFETest.DTO.coinDTO.StrategyCommonDTO;
import com.example.BeFETest.Strategy.BacktestingIndicator;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Random;
@Component
public class BacktestingAutoInd {
    private final StrategyService strategyService;

    public BacktestingAutoInd(StrategyService strategyService) {
        this.strategyService = strategyService;
    }
    // CSV 파일에 백테스트 결과 저장
    public static void logBacktestIndResult(IndicatorBasedStrategyDTO ind) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("backtesting_Ind_results.csv", true))) {
            String result = String.format("\"%s\",\"%f\",\"%f\",\"%f\",\"%f\",\"%f\",\"%d\"",
                    ind.getBacktesting_date(),
                    ind.getFinalCash(),
                    ind.getFinalAsset(),
                    ind.getFinalBalance(),
                    ind.getProfit(),
                    ind.getProfitRate(),
                    ind.getNumberOfTrades()
            );
            writer.write(result);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error writing backtest result: " + e.getMessage());
        }
    }

    // 랜덤한 전략 조건 생성
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

    // 자동 백테스팅 실행
    public void runAutomaticBacktesting(int numberOfTests) {
        for (int i = 0; i < numberOfTests; i++) {
            // 1. 랜덤 전략 생성
            IndicatorBasedStrategyDTO strategy = generateRandomInd();

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
            IndicatorBasedStrategyDTO result = BacktestingIndicator.executeTrades(commonDTO, strategy);

            // 4. 백테스트 결과 저장
            logBacktestIndResult(result);

            strategyService.saveIndicatorStrategyResult(commonDTO, (long) -3, strategy,result);
        }
    }
}

