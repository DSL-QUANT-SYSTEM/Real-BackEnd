package com.example.BeFETest.Strategy;
import com.example.BeFETest.DTO.coinDTO.EnvelopeDTO;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BacktestingEnv extends CommonFunction {
    private static final String accessKey = "YOUR_ACCESS_KEY";
    private static final String secretKey = "YOUR_SECRET_KEY";
    private static final String serverUrl = "https://api.upbit.com";

    public static void main(String[] args) {
        EnvelopeDTO Env=new EnvelopeDTO(1000000, 0.01,
                LocalDate.of(2023, 1, 1), LocalDate.of(2024, 1, 1),
                "KRW-STMX", "D", 200, 0.1,0.1, 20);

        double asset = 0;                   // 초기 자산 (BTC)

        // 캔들 데이터 가져오기
        List<Candle> candlesEnv = getCandle(Env.getTargetItem(), Env.getTickKind(), Env.getInquiryRange());
        assert candlesEnv != null;

        // 가격 데이터 추출
        List<Double> closePrices = new ArrayList<>();
        for (Candle candle : candlesEnv) {
            closePrices.add(candle.getTradePrice());
        }

        // 이동평균 및 밴드 계산
        List<Double> movingAverage = calculateMovingAverage(closePrices, Env.getMovingAveragePeriod());
        List<Double> upperBand = new ArrayList<>();
        List<Double> lowerBand = new ArrayList<>();

        for (Double avg : movingAverage) {
            upperBand.add(avg + (avg * Env.getMoving_up()));
            lowerBand.add(avg - (avg * Env.getMoving_down()));
        }

        // 매매 전략 실행
        executeEnvelopeStrategy(closePrices, movingAverage, upperBand, lowerBand, Env.getMovingAveragePeriod(),
                Env.getInitialInvestment(), asset, Env.getInitialInvestment(), Env.getTransactionFee());
    }

    // 이동평균 계산 함수
    public static List<Double> calculateMovingAverage(List<Double> closePrices, int period) {
        List<Double> movingAverage = new ArrayList<>();
        for (int i = period - 1; i < closePrices.size(); i++) {
            double sum = 0;
            for (int j = i; j > i - period; j--) {
                sum += closePrices.get(j);
            }
            movingAverage.add(sum / period);
        }
        return movingAverage;
    }

    // 매매 전략 실행 함수
    public static void executeEnvelopeStrategy(List<Double> closePrices, List<Double> movingAverage, List<Double> upperBand, List<Double> lowerBand, int period, double cash, double asset, double initialCash, double transactionFee) {
        boolean bought = false; // 매수 상태 추적 변수

        for (int i = period; i < closePrices.size(); i++) {
            double currentPrice = closePrices.get(i);
            double previousPrice = closePrices.get(i - 1);

            // 매수 조건: 주가가 하단 밴드를 하향 돌파 후 다시 상향 돌파할 때
            if (!bought && previousPrice < lowerBand.get(i - period) && currentPrice > lowerBand.get(i - period)) {
                System.out.println("Buy at " + currentPrice);
                if (cash > 0) {
                    double amountToBuy = cash / currentPrice * (1 - transactionFee);
                    asset += amountToBuy;
                    cash = 0;
                    bought = true;
                }
            }

            // 매도 조건: 주가가 상단 밴드를 상향 돌파 후 다시 하향 돌파할 때
            if (bought && previousPrice > upperBand.get(i - period) && currentPrice < upperBand.get(i - period)) {
                System.out.println("Sell at " + currentPrice);
                if (asset > 0) {
                    double amountToSell = asset * currentPrice * (1 - transactionFee);
                    cash += amountToSell;
                    asset = 0;
                    bought = false;
                }
            }
        }

        // 최종 자산 계산
        double finalBalance = cash + asset * closePrices.get(closePrices.size() - 1);
        double profitRate = ((finalBalance - initialCash) / initialCash) * 100;
        System.out.println("Initial Cash: " + initialCash);
        System.out.println("Final Balance: " + finalBalance);
        System.out.println("Profit: " + (finalBalance - initialCash));
        System.out.println("Profit Rate: " + profitRate + "%");
    }
}
