package com.example.BeFETest.Strategy;
import com.example.BeFETest.DTO.coinDTO.BollingerBandsStrategyDTO;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BacktestingBB extends CommonFunction {
    //볼린저밴드 전략 백테스팅 예시
    public static void main(String[] args) {
        BollingerBandsStrategyDTO BB=new BollingerBandsStrategyDTO(1000000, 0.01,
                LocalDate.of(2023, 1, 1), LocalDate.of(2024, 1, 1),
                "KRW-STMX", "60", 100, 20);
        // 초기 자산 설정
        double asset  = 0;       // 초기 자산 (BTC)

        //볼린저밴드 전략 예시
        List<Candle> candlesBB= getCandle(BB.getTargetItem(), BB.getTickKind(), BB.getInquiryRange());

        List<Double> closePricesBB =new ArrayList<>();
        assert candlesBB != null;
        for (Candle candle : candlesBB) {
            closePricesBB.add(candle.getTradePrice());
        }

        // 이동평균선(ma) 구하기
        List<Double> ma = calculateMovingAverage(closePricesBB, BB.getMovingAveragePeriod());
        // 표준편차로 상한하한 계산
        List<Double> std = calculateStandardDeviation(closePricesBB, BB.getMovingAveragePeriod());
        List<Double> upperBand = calculateUpperBand(ma, std);
        List<Double> lowerBand = calculateLowerBand(ma, std);

        // 밴드폭 계산
        List<Double> bandWidth = calculateBandWidth(upperBand, lowerBand, ma);
        // 밴드폭 축소 후 밀집구간 거치고 상한 돌파 시 -> 매수, 반대로 하향 이탈하면 -> 매도
        List<Boolean> buySignals = generateBuySignals(closePricesBB, upperBand, bandWidth);
        List<Boolean> sellSignals = generateSellSignals(closePricesBB, lowerBand);
        System.out.println("////////////////////////////////////BB Data////////////////////////////////////////////");
        int check=0;
        // 매수 및 매도 신호 출력
        for (int i = 0; i < closePricesBB.size(); i++) {
            if (i >= BB.getMovingAveragePeriod() - 1) { // 이동평균 및 표준편차 계산을 위해 최소 기간 길이 이후부터 출력
                if (buySignals.get(i - (BB.getMovingAveragePeriod() - 1))) {
                    System.out.println("Buy Signals: " + closePricesBB.get(i - (BB.getMovingAveragePeriod() - 1)));
                    check=1;
                }
                if (sellSignals.get(i - (BB.getMovingAveragePeriod() - 1))) {
                    System.out.println("Sell Signals: " + closePricesBB.get(i - (BB.getMovingAveragePeriod() - 1)));
                    check=1;
                }
            }
        }
        if(check==0){
            System.out.println("There are no Buy/Sell Signals");
        }

        // 백테스팅 실행
        double finalBalance = executeBackTest(buySignals, sellSignals, closePricesBB, BB.getMovingAveragePeriod(), BB.getInitialInvestment(), asset, BB.getTransactionFee());
        double profit = finalBalance - BB.getInitialInvestment();
        double profitRate = (profit / BB.getInitialInvestment()) * 100;

        // 결과 출력
        System.out.println("Initial Cash: " + BB.getInitialInvestment());
        System.out.println("Final Balance: " + finalBalance);
        System.out.println("Profit: " + profit);
        System.out.println("Profit Rate: " + profitRate + "%");
    }
    // Keys
    private static final String accessKey = "78lGs0QBrcPzJry5zDO8XhcTT7H98txHkyZBeHoT";
    private static final String secretKey = "nTOf48sFQxIyD5xwChtFxEnMKwqBsxxWCQx8G1KS";
    private static final String serverUrl = "https://api.upbit.com";

    // Constants
    private static final int minOrderAmt = 5000;

    // 매수 및 매도 로직
    public static double executeBackTest(List<Boolean> buySignals, List<Boolean> sellSignals, List<Double> closePrices, int length, double cash, double asset, double transactionFee) {
        boolean bought = false; // 매수 상태를 추적하는 변수
        for (int i = length - 1; i < closePrices.size(); i++) {
            double currentPrice = closePrices.get(i);
            if (buySignals.get(i - (length - 1)) && !bought) {
                System.out.println("Buy at " + currentPrice);
                // 매수 로직 (모든 자본으로 BTC 구매)
                if (cash > 0) {
                    double fee = cash * transactionFee; // 수수료 계산
                    double amountToInvest = cash - fee; // 수수료를 뺀 금액
                    asset += amountToInvest / currentPrice;
                    cash = 0;
                    bought = true; // 매수 상태로 설정
                }
            }
            if (sellSignals.get(i - (length - 1)) && bought) {
                System.out.println("Sell at " + currentPrice);
                // 매도 로직 (모든 BTC 판매)
                if (asset > 0) {
                    double proceeds = asset * currentPrice;
                    double fee = proceeds * transactionFee; // 수수료 계산
                    proceeds -= fee; // 수수료 차감
                    cash += proceeds;
                    asset = 0;
                    bought = false; // 매도 상태로 설정
                }
            }
        }
        return cash + asset * closePrices.get(closePrices.size() - 1);
    }


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
    // 표준편차 계산
    public static List<Double> calculateStandardDeviation(List<Double> closePrices, int period) {
        List<Double> standardDeviations = new ArrayList<>();
        for (int i = period - 1; i < closePrices.size(); i++) {
            double sum = 0;
            double mean = calculateMovingAverage(closePrices.subList(i - period + 1, i + 1), period).get(0);
            for (int j = i; j > i - period; j--) {
                sum += Math.pow(closePrices.get(j) - mean, 2);
            }
            standardDeviations.add(Math.sqrt(sum / period));
        }
        return standardDeviations;
    }

    // 상한 밴드 계산
    public static List<Double> calculateUpperBand(List<Double> ma, List<Double> std) {
        List<Double> upperBand = new ArrayList<>();
        for (int i = 0; i < ma.size(); i++) {
            upperBand.add(ma.get(i) + (2 * std.get(i)));
        }
        return upperBand;
    }

    // 하한 밴드 계산
    public static List<Double> calculateLowerBand(List<Double> ma, List<Double> std) {
        List<Double> lowerBand = new ArrayList<>();
        for (int i = 0; i < ma.size(); i++) {
            lowerBand.add(ma.get(i) - (2 * std.get(i)));
        }
        return lowerBand;
    }

    // 밴드폭 계산
    public static List<Double> calculateBandWidth(List<Double> upperBand, List<Double> lowerBand, List<Double> ma) {
        List<Double> bandWidth = new ArrayList<>();
        for (int i = 0; i < upperBand.size(); i++) {
            bandWidth.add((upperBand.get(i) - lowerBand.get(i)) / ma.get(i));
        }
        return bandWidth;
    }

    // 매수 신호 생성
    public static List<Boolean> generateBuySignals(List<Double> closePrices, List<Double> upperBand, List<Double> bandWidth) {
        List<Boolean> buySignals = new ArrayList<>();
        for (int i = 0; i < upperBand.size(); i++) {
            if (bandWidth.get(i) < 0.05 && closePrices.get(i) > upperBand.get(i)) { // 밴드폭 축소 후 상한 밴드 돌파
                buySignals.add(true);
            } else {
                buySignals.add(false);
            }
        }
        return buySignals;
    }

    // 매도 신호 생성
    public static List<Boolean> generateSellSignals(List<Double> closePrices, List<Double> lowerBand) {
        List<Boolean> sellSignals = new ArrayList<>();
        for (int i = 0; i < lowerBand.size(); i++) {
            if (closePrices.get(i) < lowerBand.get(i)) { // 하한 밴드 하향 이탈
                sellSignals.add(true);
            } else {
                sellSignals.add(false);
            }
        }
        return sellSignals;
    }
}
