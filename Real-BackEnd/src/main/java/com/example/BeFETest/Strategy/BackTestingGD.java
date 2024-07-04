package com.example.BeFETest.Strategy;

import java.util.ArrayList;
import java.util.List;

public class BackTestingGD extends  commonFunction{
    //골든데드크로스 전략 백테스팅 예시
    public static void main(String[] args) {
        // 초기 자본과 자산 설정
        double initialCash = 1000000;  // 초기 자본 (1,000,000 KRW)
        double cash=initialCash;
        double asset  = 0;       // 초기 자산 (BTC)

        // 이동평균 기간 설정
        int fastPeriod = 10;
        int slowPeriod = 50;

        // 캔들 데이터 가져오기
        List<Candle> candlesGD = getCandle("KRW-STMX", "60", 200);

        // 가격 데이터를 추출하여 closePrices 리스트에 추가
        List<Double> closePricesGD = new ArrayList<>();
        assert candlesGD != null;
        for (Candle candle : candlesGD) {
            closePricesGD.add(candle.getTradePrice());
        }

        // 이동평균 계산
        List<Double> fastMovingAverage = calculateMovingAverage(closePricesGD, fastPeriod);
        List<Double> slowMovingAverage = calculateMovingAverage(closePricesGD, slowPeriod);
        // 골든크로스 및 데드크로스 찾기
        List<Boolean> crosses = findCrosses(fastMovingAverage, slowMovingAverage);

        // 매수 및 매도 로직 실행
        executeTrades(crosses, closePricesGD, slowPeriod, cash, asset, initialCash);
    }
    // Keys
    private static final String accessKey = "78lGs0QBrcPzJry5zDO8XhcTT7H98txHkyZBeHoT";
    private static final String secretKey = "nTOf48sFQxIyD5xwChtFxEnMKwqBsxxWCQx8G1KS";
    private static final String serverUrl = "https://api.upbit.com";

    // Constants
    private static final int minOrderAmt = 5000;


    // 이동평균을 계산하는 함수
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

    // 골든크로스 및 데드크로스를 찾는 함수
    public static List<Boolean> findCrosses(List<Double> fastMovingAverage, List<Double> slowMovingAverage) {
        List<Boolean> crosses = new ArrayList<>();
        int size = Math.min(fastMovingAverage.size(), slowMovingAverage.size());
        System.out.println("////////////////////////////////////GD RESULT////////////////////////////////////////////");
        if (size < 2) {
            // slowMovingAverage의 크기가 1이면 비교할 수 없음
            System.out.println("Not enough data points to find crosses.");
            return crosses;
        }
        for (int i = 1; i < size; i++) {
            if (fastMovingAverage.get(i) > slowMovingAverage.get(i) && fastMovingAverage.get(i - 1) <= slowMovingAverage.get(i - 1)) {
                crosses.add(true); // Golden Cross
            } else if (fastMovingAverage.get(i) < slowMovingAverage.get(i) && fastMovingAverage.get(i - 1) >= slowMovingAverage.get(i - 1)) {
                crosses.add(false); // Death Cross
            } else {
                crosses.add(null); // No Cross
            }
        }
        return crosses;
    }

    // 매수 및 매도 로직
    public static void executeTrades(List<Boolean> crosses, List<Double> closePrices, int slowPeriod, double cash, double asset, double initialCash) {
        boolean bought = false; // 매수 상태를 추적하는 변수

        for (int i = 0; i < crosses.size(); i++) {
            if (crosses.get(i) != null) {
                double currentPrice = closePrices.get(i + slowPeriod - 1); // 현재 가격
                if (crosses.get(i) && !bought) {
                    System.out.println("Golden Cross at index " + (i + slowPeriod - 1) + ", Buy at " + currentPrice);
                    // 매수 로직 (모든 자본으로 BTC 구매)
                    if (cash > 0) {
                        asset += cash / currentPrice;
                        cash = 0;
                        bought = true; // 매수 상태로 설정
                    }
                } else if (!crosses.get(i) && bought) {
                    System.out.println("Death Cross at index " + (i + slowPeriod - 1) + ", Sell at " + currentPrice);
                    // 매도 로직 (모든 BTC 판매)
                    if (asset > 0) {
                        cash += asset * currentPrice;
                        asset = 0;
                        bought = false; // 매도 상태로 설정
                    }
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
