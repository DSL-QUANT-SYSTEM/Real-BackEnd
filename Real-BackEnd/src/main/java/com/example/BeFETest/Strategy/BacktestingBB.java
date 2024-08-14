package com.example.BeFETest.Strategy;
import com.example.BeFETest.DTO.coinDTO.BollingerBandsStrategyDTO;
import com.example.BeFETest.DTO.coinDTO.GoldenDeadCrossStrategyDTO;
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

public class BacktestingBB  {
    //볼린저밴드 전략 백테스팅
    // Keys
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
            if (tickKind != null) {
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
            } else {
                throw new IllegalArgumentException("틱 종류가 null입니다.");
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

    // 매수 및 매도 로직
    public static BollingerBandsStrategyDTO executeTrades(StrategyCommonDTO commonDTO, BollingerBandsStrategyDTO BB) {
        double cash = commonDTO.getInitial_investment();
        double asset  = 0;       // 초기 자산 (BTC)

        //볼린저밴드 전략 예시
        List<Candle> candlesBB= getCandle(commonDTO.getTarget_item(), commonDTO.getTick_kind(), commonDTO.getInq_range());
        List<Double> closePricesBB =new ArrayList<>();
        assert candlesBB != null;
        for (Candle candle : candlesBB) {
            closePricesBB.add(candle.getTradePrice());
        }

        // 이동평균선(ma) 구하기
        List<Double> ma = calculateMovingAverage(closePricesBB, BB.getMoveAvg());
        // 표준편차로 상한하한 계산
        List<Double> std = calculateStandardDeviation(closePricesBB, BB.getMoveAvg());
        List<Double> upperBand = calculateUpperBand(ma, std);
        List<Double> lowerBand = calculateLowerBand(ma, std);

        // 밴드폭 계산
        List<Double> bandWidth = calculateBandWidth(upperBand, lowerBand, ma);
        // 밴드폭 축소 후 밀집구간 거치고 상한 돌파 시 -> 매수, 반대로 하향 이탈하면 -> 매도
        List<Boolean> buySignals = generateBuySignals(closePricesBB, upperBand, bandWidth);
        List<Boolean> sellSignals = generateSellSignals(closePricesBB, lowerBand);
        System.out.println("////////////////////////////////////BB Result////////////////////////////////////////////");
        int check=0;
        // 매수 및 매도 신호 출력
        for (int i = 0; i < closePricesBB.size(); i++) {
            if (i >= BB.getMoveAvg() - 1) { // 이동평균 및 표준편차 계산을 위해 최소 기간 길이 이후부터 출력
                if (buySignals.get(i - (BB.getMoveAvg() - 1))) {
                    System.out.println("Buy Signals: " + closePricesBB.get(i - (BB.getMoveAvg() - 1)));
                    check=1;
                }
                if (sellSignals.get(i - (BB.getMoveAvg() - 1))) {
                    System.out.println("Sell Signals: " + closePricesBB.get(i - (BB.getMoveAvg() - 1)));
                    check=1;
                }
            }
        }
        if(check==0){
            System.out.println("There are no Buy/Sell Signals");
        }

        boolean bought = false; // 매수 상태를 추적하는 변수
        int numberOfTrades= 0;
        for (int i = BB.getMoveAvg() - 1; i < closePricesBB.size(); i++) {
            double currentPrice = closePricesBB.get(i);
            if (buySignals.get(i - (BB.getMoveAvg() - 1)) && !bought) {
                System.out.println("Buy at " + currentPrice);
                // 매수 로직 (모든 자본으로 BTC 구매)
                if (cash > 0) {
                    double fee = cash * commonDTO.getTax(); // 수수료 계산
                    double amountToInvest = cash - fee; // 수수료를 뺀 금액
                    asset += amountToInvest / currentPrice;
                    cash = 0;
                    bought = true; // 매수 상태로 설정
                    numberOfTrades++;
                }
            }
            if (sellSignals.get(i - (BB.getMoveAvg() - 1)) && bought) {
                System.out.println("Sell at " + currentPrice);
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

        double finalBalance = cash + asset * closePricesBB.getLast();
        double profit = finalBalance - commonDTO.getInitial_investment();
        double profitRate = (profit / commonDTO.getInitial_investment()) * 100;

        // 결과 출력
        System.out.println("Initial Cash: " + commonDTO.getInitial_investment());
        System.out.println("Final Balance: " + finalBalance);
        System.out.println("Profit: " + profit);
        System.out.println("Profit Rate: " + profitRate + "%");

        BB.setFinalCash(cash);
        BB.setFinalAsset(asset);
        BB.setFinalBalance(finalBalance);
        BB.setProfit(profit);
        BB.setProfitRate(profitRate);
        BB.setNumberOfTrades(numberOfTrades);

        return BB;
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
