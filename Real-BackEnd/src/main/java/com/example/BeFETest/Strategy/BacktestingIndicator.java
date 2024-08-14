package com.example.BeFETest.Strategy;
import com.example.BeFETest.DTO.coinDTO.IndicatorBasedStrategyDTO;
import com.example.BeFETest.DTO.coinDTO.StrategyCommonDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BacktestingIndicator  {
    // 초기 자본과 자산 설정
    private static final String accessKey = "78lGs0QBrcPzJry5zDO8XhcTT7H98txHkyZBeHoT";
    private static final String secretKey = "nTOf48sFQxIyD5xwChtFxEnMKwqBsxxWCQx8G1KS";
    private static final String serverUrl = "https://api.upbit.com";

    // Constants
    private static final int minOrderAmt = 5000;

    //-----------------------------------------------------------------------------
    // # - Name : send_request
    //Desc : 리퀘스트 처리
    //Input
    //1) reqType : 요청 타입
    //2) reqUrl : 요청 URL
    //3) reqParam : 요청 파라메타
    //4) reqHeader : 요청 헤더
    //Output
    //4) reponse : 응답 데이터
    //-----------------------------------------------------------------------------
    private static String sendRequest(String reqUrl) {
        try {
            // Create URL object
            URI uri = new URI(reqUrl);
            URL url = uri.toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set request method
            connection.setRequestMethod("GET");

            // Send request
            int responseCode = connection.getResponseCode();

            // Read response
            StringBuilder response = new StringBuilder();
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
            }

            // Close connection
            connection.disconnect();

            // Handle response
            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                return response.toString();
            } else if (responseCode == HttpURLConnection.HTTP_BAD_REQUEST) {
                Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.SEVERE, "Request limit exceeded! Response code: " + responseCode);
                Thread.sleep(300); // Sleep for 0.3 seconds
            } else {
                Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.SEVERE, "Other error! Response code: " + responseCode);
                Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.SEVERE, response.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Parse JSON method
    private static List<Map<String, Object>> parseJson(String response) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(response, new TypeReference<>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Setter
    @Getter
    public static class Candle {
        // Getters and Setters
        private String market;
        private String candleDateTimeUtc;
        private String candleDateTimeKst;
        private Double openingPrice;
        private Double highPrice;
        private Double lowPrice;
        private Double tradePrice;
        private Long timestamp;
        private Double candleAccTradePrice;
        private Double candleAccTradeVolume;
        private Integer unit;

        @Override
        public String toString() {
            return "Candle{" +
                    "market='" + market + '\'' +
                    ", candleDateTimeUtc='" + candleDateTimeUtc + '\'' +
                    ", candleDateTimeKst='" + candleDateTimeKst + '\'' +
                    ", openingPrice=" + openingPrice +
                    ", highPrice=" + highPrice +
                    ", lowPrice=" + lowPrice +
                    ", tradePrice=" + tradePrice +
                    ", timestamp=" + timestamp +
                    ", candleAccTradePrice=" + candleAccTradePrice +
                    ", candleAccTradeVolume=" + candleAccTradeVolume +
                    ", unit=" + unit +
                    '}';
        }
    }

    // -----------------------------------------------------------------------------
    // - Name : get_candle
    // - Desc : 캔들 조회
    // - Input
    //   1) target_item : 대상 종목
    //   2) tick_kind : 캔들 종류 (1, 3, 5, 10, 15, 30, 60, 240 - 분, D-일, W-주, M-월)
    //   3) inq_range : 조회 범위
    // - Output
    //   1) 캔들 정보 배열
    // -----------------------------------------------------------------------------
    public static List<Candle> getCandle(String targetItem, String tickKind, int inqRange) {
        try {
            String targetUrl;
            if (tickKind.equals("1") || tickKind.equals("3") || tickKind.equals("5") || tickKind.equals("10") ||
                    tickKind.equals("15") || tickKind.equals("30") || tickKind.equals("60") || tickKind.equals("240")) {
                targetUrl = "minutes/" + tickKind;
            } else if (tickKind.equals("D")) {
                targetUrl = "days";
            } else if (tickKind.equals("W")) {
                targetUrl = "weeks";
            } else if (tickKind.equals("M")) {
                targetUrl = "months";
            } else {
                throw new IllegalArgumentException("잘못된 틱 종류: " + tickKind);
            }


            String url = serverUrl + "/v1/candles/" + targetUrl + "?market=" + targetItem + "&count=" + inqRange;

            String response = sendRequest(url);
            List<Map<String, Object>> parsedData = parseJson(response);
            // Candle 객체로 변환
            List<Candle> candleData = new ArrayList<>();
            assert parsedData != null;
            for (Map<String, Object> item : parsedData) {
                Candle candle = new Candle();
                candle.setMarket(targetItem);
                candle.setCandleDateTimeUtc((String) item.get("candle_date_time_utc"));
                candle.setCandleDateTimeKst((String) item.get("candle_date_time_kst"));
                candle.setOpeningPrice(((Number) item.get("opening_price")).doubleValue());
                candle.setHighPrice(((Number) item.get("high_price")).doubleValue());
                candle.setLowPrice(((Number) item.get("low_price")).doubleValue());
                candle.setTradePrice(((Number) item.get("trade_price")).doubleValue());
                candle.setTimestamp(((Number) item.get("timestamp")).longValue());
                candle.setCandleAccTradePrice(((Number) item.get("candle_acc_trade_price")).doubleValue());
                candle.setCandleAccTradeVolume(((Number) item.get("candle_acc_trade_volume")).doubleValue());
//              candle.setUnit(((Number) item.get("unit")).intValue()); //추후에 UNIT 필요시 추가
                candleData.add(candle);
            }
            return candleData;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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
    public static IndicatorBasedStrategyDTO executeTrades(StrategyCommonDTO commonDTO, IndicatorBasedStrategyDTO ind) {
        double cash= commonDTO.getInitial_investment();
        double asset = 0;  // 초기 자산 (BTC)
        boolean bought = false; // 매수 상태를 추적하는 변수
        int numberOfTrades=0;

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

        System.out.println("////////////////////////////////////Indicator RESULT////////////////////////////////////////////");
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

            if (/*rsiBuySignal && macdBuySignal && */mfiBuySignal && !bought) {
                System.out.println("Buy Signal at index " + (i + closePrices.size() - minSize - 1) + ", Buy at " + currentPrice);
                // 매수 로직 (모든 자본으로 BTC 구매)
                if (cash > 0) {
                    double fee = cash * commonDTO.getTax(); // 수수료 계산
                    cash -= fee; // 수수료 차감
                    asset += cash / currentPrice;
                    cash = 0;
                    bought = true; // 매수 상태로 설정
                    numberOfTrades++;
                }
            } else if (/*rsiSellSignal && macdSellSignal && */ mfiSellSignal&& bought) {
                System.out.println("Sell Signal at index " + (i + closePrices.size() - minSize - 1) + ", Sell at " + currentPrice);
                // 매도 로직 (모든 BTC 판매)
                if (asset > 0) {
                    double proceeds = asset * currentPrice;
                    double fee = proceeds * commonDTO.getTax(); // 수수료 계산
                    proceeds -= fee; // 수수료 차감
                    cash += proceeds;
                    asset = 0;
                    bought = false; // 매도 상태로 설정
                    numberOfTrades++;
                }
            }
        }

        // 최종 자산 계산
        double finalBalance = cash + asset * closePrices.getLast();
        double profit=(finalBalance - commonDTO.getInitial_investment());
        double profitRate = ((finalBalance - commonDTO.getInitial_investment()) / commonDTO.getInitial_investment()) * 100;
        System.out.println("Initial Cash: " + commonDTO.getInitial_investment());
        System.out.println("Final Balance: " + finalBalance);
        System.out.println("Profit: " + profit);
        System.out.println("Profit Rate: " + profitRate + "%");

        ind.setFinalCash(cash);
        ind.setFinalAsset(asset);
        ind.setFinalBalance(finalBalance);
        ind.setProfit(profit);
        ind.setProfitRate(profitRate);
        ind.setNumberOfTrades(numberOfTrades);

        return ind;
    }
}
