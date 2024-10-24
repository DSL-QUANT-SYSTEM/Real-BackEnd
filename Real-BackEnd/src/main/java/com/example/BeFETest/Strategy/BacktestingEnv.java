package com.example.BeFETest.Strategy;
import com.example.BeFETest.DTO.coinDTO.BollingerBandsStrategyDTO;
import com.example.BeFETest.DTO.coinDTO.EnvelopeDTO;
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

public class BacktestingEnv  {
    private static final String accessKey = "YOUR_ACCESS_KEY";
    private static final String secretKey = "YOUR_SECRET_KEY";
    private static final String serverUrl = "https://api.upbit.com";


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
    public static EnvelopeDTO executeTrades(StrategyCommonDTO commonDTO, EnvelopeDTO envelopeDTO) {
        double cash = commonDTO.getInitial_investment();
        double transactionFee = commonDTO.getTax();
        double asset  = 0;       // 초기 자산 (BTC)

        List<Candle> candlesEnv= getCandle(commonDTO.getTarget_item(), commonDTO.getTick_kind(), commonDTO.getInq_range());

        // 가격 데이터 추출
        List<Double> closePrices = new ArrayList<>();
        assert candlesEnv != null;
        for (Candle candle : candlesEnv) {
            closePrices.add(candle.getTradePrice());
        }

        // 이동평균 및 밴드 계산
        List<Double> movingAverage = calculateMovingAverage(closePrices, envelopeDTO.getMovingAveragePeriod());
        List<Double> upperBand = new ArrayList<>();
        List<Double> lowerBand = new ArrayList<>();

        for (Double avg : movingAverage) {
            upperBand.add(avg + (avg * envelopeDTO.getMoving_up()*(0.1)));
            lowerBand.add(avg - (avg * envelopeDTO.getMoving_down()*(0.1)));
        }

        boolean bought = false; // 매수 상태 추적 변수
        int numberOfTrades=0;

        System.out.println("////////////////////////////////////ENV RESULT////////////////////////////////////////////");
        for (int i = envelopeDTO.getMovingAveragePeriod(); i < closePrices.size(); i++) {
            double currentPrice = closePrices.get(i);
            double previousPrice = closePrices.get(i - 1);

            // 매수 조건: 주가가 하단 밴드를 하향 돌파 후 다시 상향 돌파할 때
            if (!bought && previousPrice < lowerBand.get(i - envelopeDTO.getMovingAveragePeriod()) && currentPrice > lowerBand.get(i - envelopeDTO.getMovingAveragePeriod())) {
                System.out.println("Buy at " + currentPrice);
                if (cash > 0) {
                    double amountToBuy = cash / currentPrice * (1 - transactionFee);
                    asset += amountToBuy;
                    cash = 0;
                    bought = true;
                    numberOfTrades++;
                }
            }

            // 매도 조건: 주가가 상단 밴드를 상향 돌파 후 다시 하향 돌파할 때
            if (bought && previousPrice > upperBand.get(i - envelopeDTO.getMovingAveragePeriod()) && currentPrice < upperBand.get(i - envelopeDTO.getMovingAveragePeriod())) {
                System.out.println("Sell at " + currentPrice);
                if (asset > 0) {
                    double amountToSell = asset * currentPrice * (1 - transactionFee);
                    cash += amountToSell;
                    asset = 0;
                    bought = false;
                    numberOfTrades++;
                }
            }
        }

        // 최종 자산 계산
        double finalBalance = cash + asset * closePrices.getLast();
        double profit=(finalBalance - commonDTO.getInitial_investment());
        double profitRate;
        if(commonDTO.getInitial_investment()==0)
            profitRate=0;
        else
            profitRate = (profit / commonDTO.getInitial_investment()) * 100;
        System.out.println("Initial Cash: " + commonDTO.getInitial_investment());
        System.out.println("Final Balance: " + finalBalance);
        System.out.println("Profit: " + (finalBalance - commonDTO.getInitial_investment()));
        System.out.println("Profit Rate: " + profitRate + "%");

        envelopeDTO.setFinalCash(cash);
        envelopeDTO.setFinalAsset(asset);
        envelopeDTO.setFinalBalance(finalBalance);
        envelopeDTO.setProfit(profit);
        envelopeDTO.setProfitRate(profitRate);
        envelopeDTO.setNumberOfTrades(numberOfTrades);

        return envelopeDTO;
    }
}
