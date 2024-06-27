package com.example.BeFETest.Strategy;

import java.security.InvalidKeyException;
import java.util.*;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.SimpleFormatter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.nimbusds.jose.Algorithm;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.stream.Collectors;


import com.nimbusds.oauth2.sdk.util.CollectionUtils;

public class upbit {

    // Keys
    private static final String accessKey = "78lGs0QBrcPzJry5zDO8XhcTT7H98txHkyZBeHoT";
    private static final String secretKey = "nTOf48sFQxIyD5xwChtFxEnMKwqBsxxWCQx8G1KS";
    private static final String serverUrl = "https://api.upbit.com";

    // Constants
    private static final int minOrderAmt = 5000;

    //-----------------------------------------------------------------------------
    // - Name : set_loglevel
    //Desc : 로그레벨 설정
    //Input
    //1) level : 로그레벨
    //1. D(d) : DEBUG
    //2. E(e) : ERROR
    //3. 그외(기본) : INFO
    //Output
    //-----------------------------------------------------------------------------
    public static void setLogLevel(String level) {
        try {
            Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
            Handler consoleHandler = new ConsoleHandler();
            consoleHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(consoleHandler);

            // Set log level
            if (level.equalsIgnoreCase("D")) {
                logger.setLevel(Level.FINE);
            } else if (level.equalsIgnoreCase("E")) {
                logger.setLevel(Level.SEVERE);
            } else {
                logger.setLevel(Level.INFO);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
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
    public static String sendRequest(String reqType, String reqUrl) {
        try {
            // Create URL object
            URL url = new URL(reqUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set request method
            connection.setRequestMethod(reqType);

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

    // -----------------------------------------------------------------------------
    // - Name : get_items
    // - Desc : 전체 종목 리스트 조회
    // - Input
    //   1) market : 대상 마켓(콤마 구분자:KRW,BTC,USDT)
    //   2) except_item : 제외 종목(콤마 구분자:BTC,ETH)
    // - Output
    //   1) 전체 리스트 : 리스트
    // -----------------------------------------------------------------------------
    public static List<Map<String, Object>> getItems(String market, String exceptItem) {
        try {
            List<Map<String, Object>> rtnList = new ArrayList<>();

            // Split market and except items
            String[] markets = market.split(",");
            String[] exceptItems = exceptItem.split(",");

            String url = "https://api.upbit.com/v1/market/all";

            String response = sendRequest("GET", url);

            // Process response
            List<Map<String, Object>> data = parseJson(response);

            // Filter by market
            for (Map<String, Object> dataFor : data) {
                for (String marketFor : markets) {
                    if (dataFor.get("market").toString().split("-")[0].equals(marketFor)) {
                        rtnList.add(dataFor);
                    }
                }
            }

            // Remove except items
            for (Map<String, Object> rtnlistFor : new ArrayList<>(rtnList)) {
                for (String exceptItemFor : exceptItems) {
                    for (String marketFor : markets) {
                        if (rtnlistFor.get("market").equals(marketFor + "-" + exceptItemFor)) {
                            rtnList.remove(rtnlistFor);
                        }
                    }
                }
            }

            return rtnList;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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

//    public static class Jwt {
//
//        // JWT encoder
//        public static String encode(String payload, String secretKey) throws NoSuchAlgorithmException, InvalidKeyException {
//            String header = encodeBase64Url("{\"alg\":\"HS256\",\"typ\":\"JWT\"}");
//            String payloadEncoded = encodeBase64Url(payload);
//            String signature = generateSignature(header + "." + payloadEncoded, secretKey);
//            return header + "." + payloadEncoded + "." + signature;
//        }
//
//        private static String encodeBase64Url(String input) {
//            byte[] encodedBytes = Base64.getUrlEncoder().encode(input.getBytes(StandardCharsets.UTF_8));
//            return new String(encodedBytes, StandardCharsets.UTF_8).replace("=", "");
//        }
//
//        private static String generateSignature(String input, String secretKey) throws NoSuchAlgorithmException, InvalidKeyException {
//            try {
//                Mac hmacSHA256 = Mac.getInstance("HmacSHA256");
//                SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
//                hmacSHA256.init(secretKeySpec);
//                byte[] hash = hmacSHA256.doFinal(input.getBytes(StandardCharsets.UTF_8));
//                return encodeBase64Url(new String(hash, StandardCharsets.UTF_8));
//            } catch (NoSuchAlgorithmException | InvalidKeyException e) {
//                throw e;
//            }
//        }
//    }

    // -----------------------------------------------------------------------------
    // - Name : get_balance
    // - Desc : 주문가능 잔고 조회
    // - Input
    //   1) target_item : 대상 종목
    // - Output
    //   2) rtn_balance : 주문가능 잔고
    // -----------------------------------------------------------------------------
//    public static double getBalance(String targetItem) {
//        try {
//            // 주문가능 잔고 변수
//            double rtnBalance = 0;
//
//            // 최대 재시도 횟수
//            int maxCnt = 0;
//
//            // 잔고가 조회될 때까지 반복
//            while (true) {
//                // 재시도 횟수 증가
//                maxCnt++;
//
//                // 페이로드 생성
//                String nonce = UUID.randomUUID().toString();
//                String payload = String.format("{\"access_key\":\"%s\",\"nonce\":\"%s\"}", accessKey, nonce);
//
//                // 페이로드 인코딩
//                Algorithm algorithm = Algorithm.parse(secretKey);
//
//                String jwtToken = Jwt.create()
//                        .withClaim("access_key", accessKey)
//                        .withClaim("nonce", UUID.randomUUID().toString())
//                        .sign(algorithm);
//
//                String authenticationToken = "Bearer " + jwtToken;
//
//                // 잔고 조회를 위한 요청 보내기
//                String res = sendRequest("GET", serverUrl + "/v1/accounts");
//
//                List<Map<String, Object>> myAsset = parseJson(res);
//
//                // 대상 종목의 잔고 조회
//                for (Map<String, Object> myAssetFor : myAsset) {
//                    if (myAssetFor.get("currency").equals(targetItem.split("-")[1])) {
//                        rtnBalance = Double.parseDouble(myAssetFor.get("balance").toString());
//                    }
//                }
//
//                // 잔고가 0 이상일 때까지 반복
//                if (rtnBalance > 0) {
//                    break;
//                }
//
//                // 최대 100회까지 재시도
//                if (maxCnt > 100) {
//                    break;
//                }
//
//                System.out.println("[주문가능 잔고 조회] 요청 재시도 중...");
//            }
//
//            return rtnBalance;
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return 0; // 또는 예외 처리
//        }
//    }

    public static class Candle {
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

        // Getters and Setters
        public String getMarket() {
            return market;
        }

        public void setMarket(String market) {
            this.market = market;
        }

        public String getCandleDateTimeUtc() {
            return candleDateTimeUtc;
        }

        public void setCandleDateTimeUtc(String candleDateTimeUtc) {
            this.candleDateTimeUtc = candleDateTimeUtc;
        }

        public String getCandleDateTimeKst() {
            return candleDateTimeKst;
        }

        public void setCandleDateTimeKst(String candleDateTimeKst) {
            this.candleDateTimeKst = candleDateTimeKst;
        }

        public Double getOpeningPrice() {
            return openingPrice;
        }

        public void setOpeningPrice(Double openingPrice) {
            this.openingPrice = openingPrice;
        }

        public Double getHighPrice() {
            return highPrice;
        }

        public void setHighPrice(Double highPrice) {
            this.highPrice = highPrice;
        }

        public Double getLowPrice() {
            return lowPrice;
        }

        public void setLowPrice(Double lowPrice) {
            this.lowPrice = lowPrice;
        }

        public Double getTradePrice() {
            return tradePrice;
        }

        public void setTradePrice(Double tradePrice) {
            this.tradePrice = tradePrice;
        }

        public Long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(Long timestamp) {
            this.timestamp = timestamp;
        }

        public Double getCandleAccTradePrice() {
            return candleAccTradePrice;
        }

        public void setCandleAccTradePrice(Double candleAccTradePrice) {
            this.candleAccTradePrice = candleAccTradePrice;
        }

        public Double getCandleAccTradeVolume() {
            return candleAccTradeVolume;
        }

        public void setCandleAccTradeVolume(Double candleAccTradeVolume) {
            this.candleAccTradeVolume = candleAccTradeVolume;
        }

        public Integer getUnit() {
            return unit;
        }

        public void setUnit(Integer unit) {
            this.unit = unit;
        }

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

            String response = sendRequest("GET", url);
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
//                candle.setUnit(((Number) item.get("unit")).intValue()); //추후에 UNIT 필요시 추가
                candleData.add(candle);
            }
            return candleData;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // -----------------------------------------------------------------------------
    // - Name : get_ticker
    // - Desc : 현재가 조회
    // - Input
    //  1) target_itemlist : 대상 종목(콤마 구분자)
    // - Output
    //   2) 현재가 데이터
    //-----------------------------------------------------------------------------
    public static List<Map<String, Object>> getTicker(String targetItem) {
        try {
            String url = "https://api.upbit.com/v1/ticker?markets="+targetItem;
            String response = sendRequest("GET", url);
            return parseJson(response);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Double> getRSI(List<Candle> candles, int period) {
        List<Double> rsiValues = new ArrayList<>();

        if (candles.size() < period + 1) {
            return rsiValues; // Not enough data to calculate RSI
        }

        List<Double> gains = new ArrayList<>(); // 상승 리스트
        List<Double> losses = new ArrayList<>();// 하락 리스트

        // 데이터 정렬 (과거부터 현재 순서로)
        candles = candles.stream()
                .sorted(Comparator.comparing(c -> c.candleDateTimeKst))
                .collect(Collectors.toList());

        for (int i = 0; i < candles.size()-1; i++) {
            double gap = candles.get(i+1).tradePrice - candles.get(i).tradePrice;
            if (gap > 0) {// 종가가 전일 종가보다 상승일 경우
                gains.add(gap);
                losses.add(0.0);
            } else if(gap < 0){// 종가가 전일 종가보다 하락일 경우
                gains.add(0.0);
                losses.add(gap * -1); // 음수를 양수로 변환해준다.
            }else{// 상승, 하락이 없을 경우 종가 - 전일 종가 = gap은 0이므로 0값을 넣어줍니다.
                gains.add(0.0);
                losses.add(0.0);
            }
        }

        double avgGain = gains.subList(0, period).stream().mapToDouble(a -> a).average().orElse(0.0);
        double avgLoss = losses.subList(0, period).stream().mapToDouble(a -> a).average().orElse(0.0);

        for (int i = period; i < gains.size(); i++) {
            avgGain = (avgGain * (period - 1) + gains.get(i)) / period;
            avgLoss = (avgLoss * (period - 1) + losses.get(i)) / period;

            double rs = avgGain / avgLoss;
            double rsi = 100*(rs/(1+rs));
            rsiValues.add(rsi);
        }

        return rsiValues;
    }


    private static List<Double> getMFI(String targetItem, String tickKind, int inqRange, int loopCnt) {
        try {
            List<List<Candle>> candleDatas = new ArrayList<>();
            List<Double> mfiList = new ArrayList<>();

            // 캔들 데이터를 가져옴
            List<Candle> candleData = getCandle(targetItem, tickKind, inqRange);

            // 조회 횟수별 candle 데이터 조합
            for (int i = 0; i < loopCnt; i++) {
                candleDatas.add(candleData.subList(i, candleData.size()));
            }

            // 캔들 데이터만큼 MFI 계산
            for (List<Candle> candles : candleDatas) {
                if (candles.size() < 15) continue;  // 최소 15개의 데이터 필요

                double positiveMF = 0;
                double negativeMF = 0;

                for (int i = 0; i < 14; i++) {
                    Candle current = candles.get(i);
                    Candle previous = candles.get(i + 1);

                    double typicalPriceCurrent = (current.tradePrice + current.highPrice + current.lowPrice) / 3;
                    double typicalPricePrevious = (previous.tradePrice + previous.highPrice + previous.lowPrice) / 3;
                    double moneyFlow = typicalPriceCurrent * current.candleAccTradeVolume;

                    if (typicalPriceCurrent > typicalPricePrevious) {
                        positiveMF += moneyFlow;
                    } else if (typicalPriceCurrent < typicalPricePrevious) {
                        negativeMF += moneyFlow;
                    }
                }

                double mfi = (negativeMF > 0) ? 100 - (100 / (1 + (positiveMF / negativeMF))) : 100;
                mfiList.add(mfi);

            }

            return mfiList;
        } catch
        (Exception e) {
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

    public static List<Double> calculateMACD(List<Double> closePrices) {
        List<Double> shortEma = calculateEMA(closePrices, 12);
        List<Double> longEma = calculateEMA(closePrices, 26);
        List<Double> macValues = new ArrayList<>();

        int minSize = Math.min(shortEma.size(), longEma.size());
        for (int i = 0; i < minSize; i++) {
            macValues.add(shortEma.get(i) - longEma.get(i));
        }
        return macValues;
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
                double emaValue = ((prices.get(i) - ema.get(ema.size() - 1)) * multiplier) + ema.get(ema.size() - 1);
                ema.add(emaValue);
            }
        }
        return ema;
    }

    public static void main(String[] args) {
        //로그 레벨 설정
        setLogLevel("D");
        //이더리움 제외 KRW마켓 종목들 가져오기
        List<Map<String, Object>> items = getItems("KRW", "ETH");
        //가져온 종목들 출력
        if (items != null) {
            for (Map<String, Object> item : items) {
                System.out.println(item);
            }
        }

//        double balance = getBalance("KRW-BTC");
//        System.out.println("Balance: " + balance);

        System.out.println("////////////////////////////////////Candle Data////////////////////////////////////////////");
        //BTC-USDT의 1분봉 기준 10개의 캔들 데이터 조회
        String targetCandle = "KRW-BTC"; // 대상 종목
        String tickKind = "5"; // 캔들 종류 (분단위)
        int inqRange = 3; // 조회 범위

        List<Candle> candles = getCandle(targetCandle, tickKind, inqRange);
        if (candles != null) {
            for (Candle candle : candles) {
                System.out.println("Market: " + candle.getMarket());
                System.out.println("Candle Date Time UTC: " + candle.getCandleDateTimeUtc());
                System.out.println("Candle Date Time KST: " + candle.getCandleDateTimeKst());
                System.out.println("Opening Price: " + candle.getOpeningPrice());
                System.out.println("High Price: " + candle.getHighPrice());
                System.out.println("Low Price: " + candle.getLowPrice());
                System.out.println("Trade Price: " + candle.getTradePrice());
                System.out.println("Candle Accumulated Trade Volume: " + candle.getCandleAccTradeVolume());
                System.out.println("Candle Accumulated Trade Price: " + candle.getCandleAccTradePrice());
                System.out.println("Timestamp: " + candle.getTimestamp());
                System.out.println("------------------------------------------");
            }
        } else {
            System.out.println("Failed to get candle data.");
        }

        System.out.println("////////////////////////////////////Ticker Data////////////////////////////////////////////");
        //현재가 조회할 종목 설정
        String targetTicker = "KRW-BTC";

        // getTicker 메소드를 사용하여 현재가 조회
        List<Map<String, Object>> tickerData = getTicker(targetTicker);

        // 조회된 데이터 출력
        if (tickerData != null) {
            System.out.println(targetTicker+" Ticker Data: " + tickerData);
        } else {
            System.out.println("Failed to get Ticker Data.");
        }

        //RSI 보조지표 조회
        String marketRSI = "KRW-BTC";
        String tickKindRSI = "D";  // 일간 캔들
        int period = 14;  // RSI 계산을 위한 기간
        int inqRangeRSI = 20; // 조회 범위

        List<Candle> candlesRSI = getCandle(marketRSI, tickKindRSI, inqRangeRSI);
        if (candlesRSI != null) {
            List<Double> rsiValues = getRSI(candlesRSI, period);
            System.out.println("////////////////////////////////////RSI Data////////////////////////////////////////////");
            for (int i = 0; i < rsiValues.size(); i++) {
                System.out.println("Date: " + candlesRSI.get(i).candleDateTimeKst + " - RSI: " + rsiValues.get(i));
            }
        }

        // MFI 지표 조회
        String marketMFI = "KRW-BTC";
        String tickKindMFI = "D";  // 일간 캔들
        int inqRangeMFI = 20;  // 조회 범위
        int loopCnt = 5;  // 반복 계산 횟수
        //loopCnt와 period(14)를 더한 값 이상의 값으로 조회 범위를 설정해야함

        List<Double> mfiValues = getMFI(marketMFI, tickKindMFI, inqRangeMFI, loopCnt);
        if (mfiValues != null) {
            System.out.println("////////////////////////////////////MFI Data////////////////////////////////////////////");
            for (int i = 0; i < mfiValues.size(); i++) {
                assert candlesRSI != null;
                System.out.println("Date: " + candlesRSI.get(i).candleDateTimeKst + " - MFI: " + mfiValues.get(i));
            }
        }

        //MACD 지표 조회
        String marketMACD = "KRW-BTC";
        String tickKindMACD = "D";  // 일간 캔들
        int inqRangeMACD = 20;  // 조회 범위

        List<Candle> candlesMACD=getCandle(marketMACD,tickKindMACD,inqRangeMACD);

        // 가격 데이터를 추출하여 closePrices 리스트에 추가
        List<Double> closePricesMACD = new ArrayList<>();
        assert candlesMACD != null;
        for (Candle candle : candlesMACD) {
            closePricesMACD.add(candle.getTradePrice());
        }

        if (!closePricesMACD.isEmpty()) {
            List<Double> macValues = calculateMACD(closePricesMACD);
            List<Double> macSignal = calculateSignal(macValues, 9); // 9-period signal line
            System.out.println("////////////////////////////////////MACD Data////////////////////////////////////////////");
            // Print MACD and Signal values
            for (int i = 0; i < macValues.size(); i++) {
                System.out.println("MACD: " + macValues.get(i) + ", Signal: " + macSignal.get(i));
            }
        }


        //골든데드크로스 전략 예시
        // 이동평균 기간 설정
        int fastPeriod = 10;
        int slowPeriod = 50;

        // 캔들 데이터 가져오기
        List<Candle> candlesGD = getCandle("KRW-BTC", "D", 200);

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

        // 전략에 따른 매수 및 매도
        for (int i = 0; i < crosses.size(); i++) {
            if (crosses.get(i) != null) {
                if (crosses.get(i)) {
                    System.out.println("Golden Cross at index " + (i + slowPeriod - 1) + ", Buy");
                } else {
                    System.out.println("Death Cross at index " + (i + slowPeriod - 1) + ", Sell");
                }
            }
        }

        //볼린저밴드 전략 예시
        List<Candle> candlesBB= getCandle("KRW-BTC", "D", 100);
        int length = 20; // 이동평균 기간

        List<Double> closePricesBB =new ArrayList<>();
        assert candlesBB != null;
        for (Candle candle : candlesBB) {
            closePricesBB.add(candle.getTradePrice());
        }

        // 이동평균선(ma) 구하기
        List<Double> ma = calculateMovingAverage(closePricesBB, length);
        // 표준편차로 상한하한 계산
        List<Double> std = calculateStandardDeviation(closePricesBB, length);
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
            if (i >= length - 1) { // 이동평균 및 표준편차 계산을 위해 최소 기간 길이 이후부터 출력
                if (buySignals.get(i - (length - 1))) {
                    System.out.println("Buy Signals: " + closePricesBB.get(i - (length - 1)));
                    check=1;
                }
                if (sellSignals.get(i - (length - 1))) {
                    System.out.println("Sell Signals: " + closePricesBB.get(i - (length - 1)));
                    check=1;
                }
            }
        }
        if(check==0){
            System.out.println("There are no Buy/Sell Signals");
        }
    }
}

