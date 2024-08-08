package com.example.BeFETest.Strategy;
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

public class BacktestingGD {
    //골든데드크로스 전략 백테스팅 예시
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


//            String url = serverUrl + "/v1/candles/" + targetUrl + "?market=" + targetItem + "&count=" + inqRange;
            String url = serverUrl + "/v1/candles/" + targetUrl + "?market=" + "KRW-STMX" + "&count=" + inqRange;

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
    public static GoldenDeadCrossStrategyDTO executeTrades(StrategyCommonDTO commonDTO, GoldenDeadCrossStrategyDTO gd) {
        double cash = commonDTO.getInitial_investment();
        double asset = 0;       // 초기 자산 (BTC)

        // 캔들 데이터 가져오기
        List<Candle> candlesGD = getCandle(commonDTO.getTarget_item(), commonDTO.getTick_kind(), commonDTO.getInq_range());

        // 가격 데이터를 추출하여 closePrices 리스트에 추가
        List<Double> closePricesGD = new ArrayList<>();
        assert candlesGD != null;
        for (Candle candle : candlesGD) {
            closePricesGD.add(candle.getTradePrice());
        }

        // 이동평균 계산
        List<Double> fastMovingAverage = calculateMovingAverage(closePricesGD, gd.getFastMoveAvg());
        List<Double> slowMovingAverage = calculateMovingAverage(closePricesGD, gd.getSlowMoveAvg());
        // 골든크로스 및 데드크로스 찾기
        List<Boolean> crosses = findCrosses(fastMovingAverage, slowMovingAverage);

        // 매수 및 매도 로직 실행
        boolean bought = false; // 매수 상태를 추적하는 변수
        int numberOfTrades=0;
        for (int i = 0; i < crosses.size(); i++) {
            if (crosses.get(i) != null) {
                double currentPrice = closePricesGD.get(i + gd.getSlowMoveAvg() - 1); // 현재 가격
                if (crosses.get(i) && !bought) {
                    System.out.println("Golden Cross at index " + (i + gd.getSlowMoveAvg() - 1) + ", Buy at " + currentPrice);
                    // 매수 로직 (모든 자본으로 BTC 구매)
                    if (cash > 0) {
                        double fee = cash * commonDTO.getTax(); // 수수료 계산
                        cash -= fee; // 수수료 차감
                        asset += cash / currentPrice;
                        cash = 0;
                        bought = true; // 매수 상태로 설정
                        numberOfTrades++;
                    }
                } else if (!crosses.get(i) && bought) {
                    System.out.println("Death Cross at index " + (i + gd.getSlowMoveAvg() - 1) + ", Sell at " + currentPrice);
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
        }

        // 최종 자산 계산
        double finalBalance = cash + asset * closePricesGD.getLast();
        double profit=(finalBalance - commonDTO.getInitial_investment());
        double profitRate = ((finalBalance - commonDTO.getInitial_investment()) / commonDTO.getInitial_investment()) * 100;
        System.out.println("Initial Cash: " + commonDTO.getInitial_investment());
        System.out.println("Final Balance: " + finalBalance);
        System.out.println("Profit: " + profit);
        System.out.println("Profit Rate: " + profitRate + "%");

        gd.setFinalCash(cash);
        gd.setFinalAsset(asset);
        gd.setFinalBalance(finalBalance);
        gd.setProfit(profit);
        gd.setProfitRate(profitRate);
        gd.setNumberOfTrades(numberOfTrades);

        return gd;
    }
}
