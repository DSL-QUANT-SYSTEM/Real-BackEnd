package com.example.BeFETest.Strategy;

import java.security.InvalidKeyException;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.SimpleFormatter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.nimbusds.jose.Algorithm;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.UUID;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

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
                candle.setUnit(((Number) item.get("unit")).intValue());
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
                System.out.println("Unit: " + candle.getUnit());
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

    }
}

