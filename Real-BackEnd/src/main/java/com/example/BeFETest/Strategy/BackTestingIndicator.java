package com.example.BeFETest.Strategy;
import com.example.BeFETest.DTO.coinDTO.BollingerBandsStrategyDTO;
import com.example.BeFETest.DTO.coinDTO.IndicatorBasedStrategyDTO;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BackTestingIndicator extends commonFunction {
    // 초기 자본과 자산 설정
    public static void main(String[] args) {
        IndicatorBasedStrategyDTO IB =new IndicatorBasedStrategyDTO(1000000, 0.01,
                LocalDate.of(2023, 1, 1), LocalDate.of(2024, 1, 1),
                "KRW-BTC", "5", 200, 14);

        double asset = 0;  // 초기 자산 (BTC)
        boolean bought = false; // 매수 상태를 추적하는 변수

        // 캔들 데이터 가져오기
        List<Candle> candles = getCandle("KRW-BTC", "5", 200);
        assert candles != null;

        // RSI, MFI, MACD 지표 계산
        List<Double> closePrices = new ArrayList<>();
        for (Candle candle : candles) {
            closePrices.add(candle.getTradePrice());
        }

        int period = 14;
        List<Double> rsiValues = calculateRSI(candles, period);
        List<Double> mfiValues = calculateMFI(candles,period);
        List<Double> macdValues = calculateMACD(closePrices);
        List<Double> signalValues = calculateSignal(macdValues, 9);

        executeTrades(rsiValues, mfiValues, macdValues, signalValues, closePrices, IB.getInitialInvestment(), asset, IB.getInitialInvestment(), bought, IB.getTransactionFee());
    }

    // RSI 계산 메소드
    private static List<Double> calculateRSI(List<Candle> candles, int period) {
        List<Double> rsiValues = new ArrayList<>();
        List<Double> gains = new ArrayList<>();
        List<Double> losses = new ArrayList<>();

        for (int i = 1; i < candles.size(); i++) {
            double change = candles.get(i).getTradePrice() - candles.get(i - 1).getTradePrice();
            if (change > 0) {
                gains.add(change);
                losses.add(0.0);
            } else {
                gains.add(0.0);
                losses.add(-change);
            }
        }

        double avgGain = gains.subList(0, period).stream().mapToDouble(a -> a).average().orElse(0.0);
        double avgLoss = losses.subList(0, period).stream().mapToDouble(a -> a).average().orElse(0.0);

        for (int i = period; i < gains.size(); i++) {
            avgGain = (avgGain * (period - 1) + gains.get(i)) / period;
            avgLoss = (avgLoss * (period - 1) + losses.get(i)) / period;

            double rs = avgGain / avgLoss;
            double rsi = 100 - (100 / (1 + rs));
            rsiValues.add(rsi);
        }

        return rsiValues;
    }

    // MFI 계산 메소드
    private static List<Double> calculateMFI(List<Candle> candles, int period) {
        List<Double> mfiValues = new ArrayList<>();

        // Typical Price, Positive Money Flow, Negative Money Flow 초기화
        double[] typicalPrices = new double[candles.size()];
        double[] positiveMF = new double[candles.size()];
        double[] negativeMF = new double[candles.size()];

        for (int i = 0; i < candles.size(); i++) {
            Candle current = candles.get(i);
            typicalPrices[i] = (current.getTradePrice() + current.getHighPrice() + current.getLowPrice()) / 3.0;

            if (i > 0) {
                if (typicalPrices[i] > typicalPrices[i - 1]) {
                    positiveMF[i] = typicalPrices[i] * current.getCandleAccTradeVolume();
                } else if (typicalPrices[i] < typicalPrices[i - 1]) {
                    negativeMF[i] = typicalPrices[i] * current.getCandleAccTradeVolume();
                }
            }
        }

        for (int i = period; i < candles.size(); i++) {
            double sumPositiveMF = 0;
            double sumNegativeMF = 0;

            // 최근 n일 동안의 긍정적 및 부정적 Money Flow의 합을 계산
            for (int j = i - period + 1; j <= i; j++) {
                sumPositiveMF += positiveMF[j];
                sumNegativeMF += negativeMF[j];
            }

            // MFI 계산
            double mfi;
            if (sumNegativeMF > 0) {
                mfi = 100 - (100 / (1 + (sumPositiveMF / sumNegativeMF)));
            } else {
                mfi = 100; // 부정적 Money Flow가 없을 경우 MFI를 100으로 설정
            }

            mfiValues.add(mfi);
        }

        return mfiValues;
    }

    // MACD 계산 메소드
    public static List<Double> calculateMACD(List<Double> closePrices) {
        List<Double> shortEma = calculateEMA(closePrices, 12);
        List<Double> longEma = calculateEMA(closePrices, 26);
        List<Double> macdValues = new ArrayList<>();

        int minSize = Math.min(shortEma.size(), longEma.size());
        for (int i = 0; i < minSize; i++) {
            macdValues.add(shortEma.get(i) - longEma.get(i));
        }
        return macdValues;
    }

    public static List<Double> calculateSignal(List<Double> macd, int period) {
        return calculateEMA(macd, period);
    }

    public static List<Double> calculateEMA(List<Double> prices, int period) {
        List<Double> ema = new ArrayList<>();
        double multiplier = 2.0 / (period + 1);
        double sum = 0;

        for (int i = 0; i < prices.size(); i++) {
            if (i < period) {
                sum += prices.get(i);
                if (i == period - 1) {
                    ema.add(sum / period);
                }
            } else {
                double emaValue = ((prices.get(i) - ema.getLast()) * multiplier) + ema.getLast();
                ema.add(emaValue);
            }
        }
        return ema;
    }

    // 매수 및 매도 로직
    public static void executeTrades(List<Double> rsiValues, List<Double> mfiValues, List<Double> macdValues, List<Double> signalValues,
                                     List<Double> closePrices, double cash, double asset, double initialCash, boolean bought, double transactionFee) {
        int minSize = Math.min(Math.min(rsiValues.size(), mfiValues.size()), Math.min(macdValues.size(), signalValues.size()));
        for (int i = 3; i < minSize; i++) {
            double currentPrice = closePrices.get(i + closePrices.size() - minSize - 1); // 현재 가격

            //복합 매수 로직
//            boolean rsiBuySignal = rsiValues.get(i - 2) < 30 && rsiValues.get(i - 3) < rsiValues.get(i - 2) && rsiValues.get(i - 1) > rsiValues.get(i - 2) && rsiValues.get(i) > rsiValues.get(i - 1);
//            boolean mfiBuySignal = mfiValues.get(i - 2) < 20 && mfiValues.get(i - 3) < mfiValues.get(i - 2) && mfiValues.get(i - 1) > mfiValues.get(i - 2) && mfiValues.get(i) > mfiValues.get(i - 1);
//            boolean macdBuySignal = macdValues.get(i - 3) < 0 && macdValues.get(i - 2) < 0 && macdValues.get(i - 1) < 0 && macdValues.get(i - 3) < macdValues.get(i - 2) && macdValues.get(i - 1) > macdValues.get(i - 2) && macdValues.get(i) > macdValues.get(i - 1);

            //각 지표 매수 시그널
            boolean rsiBuySignal = rsiValues.get(i) < 30.0 ;// RSI가 30미만이면 매수 신호
            boolean mfiBuySignal = mfiValues.get(i) < 20.0 ; //MFI가 20미만이면 매수 신호
            boolean macdBuySignal = macdValues.get(i - 3) < 0.0 && macdValues.get(i - 2) < 0.0 && macdValues.get(i - 1) < 0.0
                    && macdValues.get(i - 3) > macdValues.get(i - 2) && macdValues.get(i - 1) > macdValues.get(i - 2) && macdValues.get(i) > macdValues.get(i - 1);
            //각 지표 매도 시그널
            boolean rsiSellSignal = rsiValues.get(i) > 70.0; // RSI가 70을 초과하면 매도 신호
            boolean mfiSellSignal = mfiValues.get(i) > 80.0; // MFI가 80을 초과하면 매도 신호
            boolean macdSellSignal = macdValues.get(i) < 0.0 && macdValues.get(i - 1) < 0.0 && macdValues.get(i - 2) < 0.0
                    && macdValues.get(i) > macdValues.get(i - 1) && macdValues.get(i - 1) > macdValues.get(i - 2); // MACD 조건

            if (mfiBuySignal && !bought) {
                System.out.println("Buy Signal at index " + (i + closePrices.size() - minSize - 1) + ", Buy at " + currentPrice);
                // 매수 로직 (모든 자본으로 BTC 구매)
                if (cash > 0) {
                    double fee = cash * transactionFee; // 수수료 계산
                    cash -= fee; // 수수료 차감
                    asset += cash / currentPrice;
                    cash = 0;
                    bought = true; // 매수 상태로 설정
                }
            } else if (mfiSellSignal&& bought) {
                System.out.println("Sell Signal at index " + (i + closePrices.size() - minSize - 1) + ", Sell at " + currentPrice);
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

        // 최종 자산 계산
        double finalBalance = cash + asset * closePrices.getLast();
        double profitRate = ((finalBalance - initialCash) / initialCash) * 100;
        System.out.println("Initial Cash: " + initialCash);
        System.out.println("Final Balance: " + finalBalance);
        System.out.println("Profit: " + (finalBalance - initialCash));
        System.out.println("Profit Rate: " + profitRate + "%");
    }
}
