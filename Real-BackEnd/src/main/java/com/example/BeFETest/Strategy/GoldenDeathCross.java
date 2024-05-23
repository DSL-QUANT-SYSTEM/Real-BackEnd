package com.example.BeFETest.Strategy;

import java.util.ArrayList;
import java.util.List;

public class GoldenDeathCross {
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
        for (int i = 1; i < fastMovingAverage.size(); i++) {
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

    public static void main(String[] args) {
        // 이동평균 기간 설정
        int fastPeriod = 50;
        int slowPeriod = 200;

        // 가격 데이터와 이동평균 계산
        List<Double> closePrices = new ArrayList<>(); // 가격 데이터
        // closePrices 리스트에 가격 데이터를 추가하는 코드가 필요합니다.

        // 이동평균 계산
        List<Double> fastMovingAverage = calculateMovingAverage(closePrices, fastPeriod);
        List<Double> slowMovingAverage = calculateMovingAverage(closePrices, slowPeriod);

        // 골든크로스 및 데드크로스 찾기
        List<Boolean> crosses = findCrosses(fastMovingAverage, slowMovingAverage);

        // 전략에 따른 매수 및 매도
        for (int i = 0; i < crosses.size(); i++) {
            if (crosses.get(i) != null) {
                if (crosses.get(i)) {
                    // 매수 로직
                } else {
                    // 매도 로직
                }
            }
        }
    }
}
