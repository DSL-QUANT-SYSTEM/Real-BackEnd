package com.example.BeFETest.Strategy;
import com.example.BeFETest.DTO.coinDTO.GoldenDeadCrossStrategyDTO;
import com.example.BeFETest.DTO.coinDTO.StrategyCommonDTO;
import com.example.BeFETest.DTO.coinDTO.WilliamsDTO;
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
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BacktestingW {
    //윌리엄스 전략 백테스팅 예시
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



    // 윌리엄스 %R 계산
    public static double calculateWilliamsR(List<Candle> candles, int index, int n) {
        double high = Double.MIN_VALUE;
        double low = Double.MAX_VALUE;

        for (int i = index; i < index + n; i++) {
            Candle candle = candles.get(i);
            if (candle.getHighPrice() > high) {
                high = candle.getHighPrice();
            }
            if (candle.getLowPrice() < low) {
                low = candle.getLowPrice();
            }
        }
        double close = candles.get(index).getTradePrice();
        return ((high - close) / (high - low)) * (-100);
    }

    // 매수 및 매도 로직
    public static WilliamsDTO executeTrades(StrategyCommonDTO commonDTO, WilliamsDTO williamsDTO) {
        List<Candle> candles = getCandle(commonDTO.getTarget_item(), commonDTO.getTick_kind(), commonDTO.getInq_range());

        // 가격 데이터를 추출하여 closePrices 리스트에 추가
        List<Double> closePricesW = new ArrayList<>();
        assert candles != null;
        for (Candle candle : candles) {
            closePricesW.add(candle.getTradePrice());
        }

        boolean bought = false;
        double cash = commonDTO.getInitial_investment();
        double asset = 0;
        int daysSinceExtreme = -1;
        double extremeValue = -100;
        int numberOfTrades=0;

        for (int i = 0; i < Objects.requireNonNull(candles).size() - williamsDTO.getWilliamsPeriod(); i++) {
            double williamsR = calculateWilliamsR(candles, i, williamsDTO.getWilliamsPeriod());

            // 매수 로직
            if (williamsR == -100) {
                daysSinceExtreme = 0;
                extremeValue = williamsR;
            } else if (daysSinceExtreme >= 0) {
                daysSinceExtreme++;
                if (daysSinceExtreme == 5 && williamsR >= -95 && extremeValue == -100 && !bought) {
                    double price = candles.get(i).getTradePrice();
                    asset = cash / price;
                    cash = 0;
                    bought = true;
                    numberOfTrades++;
                    System.out.println("매수: " + price + "에 매수");
                }
            }

            // 매도 로직
            if (williamsR == 0) {
                daysSinceExtreme = 0;
                extremeValue = williamsR;
            } else if (daysSinceExtreme >= 0) {
                daysSinceExtreme++;
                if (daysSinceExtreme == 5 && williamsR <= -5 && extremeValue == 0 && bought) {
                    double price = candles.get(i).getTradePrice();
                    cash = asset * price;
                    asset = 0;
                    bought = false;
                    numberOfTrades++;
                    System.out.println("매도: " + price + "에 매도");
                }
            }
        }

        // 최종 자산 계산
        double finalBalance = cash + asset * closePricesW.getLast();
        double profit=(finalBalance - commonDTO.getInitial_investment());
        double profitRate;
        if(commonDTO.getInitial_investment()==0)
            profitRate=0;
        else
            profitRate = (profit / commonDTO.getInitial_investment()) * 100;
        System.out.println("Initial Cash: " + commonDTO.getInitial_investment());
        System.out.println("Final Balance: " + finalBalance);
        System.out.println("Profit: " + profit);
        System.out.println("Profit Rate: " + profitRate + "%");

        williamsDTO.setFinalCash(cash);
        williamsDTO.setFinalAsset(asset);
        williamsDTO.setFinalBalance(finalBalance);
        williamsDTO.setProfit(profit);
        williamsDTO.setProfitRate(profitRate);
        williamsDTO.setNumberOfTrades(numberOfTrades);

        return williamsDTO;
    }
}
