package com.example.BeFETest.controller;

import com.example.BeFETest.BusinessLogicLayer.BacktestingAuto.*;
import com.example.BeFETest.BusinessLogicLayer.Strategy.StrategyService;
import com.example.BeFETest.BusinessLogicLayer.coin.CoinService;
import com.example.BeFETest.BusinessLogicLayer.kosdaq.KosdaqResponseService;
import com.example.BeFETest.BusinessLogicLayer.kospi.Kospi200ResponseService;
import com.example.BeFETest.BusinessLogicLayer.kospi.KospiResponseService;
import com.example.BeFETest.DTO.CoinDetailsDTO;
import com.example.BeFETest.DTO.SchedulingCoin.SchedulingCoinDTO;
import com.example.BeFETest.DTO.kosdaq.KosdaqResponseDTO;
import com.example.BeFETest.DTO.kospi.KospiResponseDTO;
import com.example.BeFETest.DTO.kospi200.Kospi200ResponseDTO;
import com.example.BeFETest.Entity.BacktestingRes.*;
import com.example.BeFETest.JWT.JwtUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DataController {
    ///api/kosdak/last-year

    private final KosdaqResponseService kosdaqService;
    private final KospiResponseService kospiService;
    private final Kospi200ResponseService kospi200Service;

    private final CoinService coinService;

    private final StrategyService strategyService;  // StrategyService 주입
    private final BacktestingAutoGD backtestingAutoGD;
    private final BacktestingAutoBB backtestingAutoBB;
    private final BacktestingAutoInd backtestingAutoInd;
    private final BacktestingAutoEnv backtestingAutoEnv;
    private final BacktestingAutoWilliams backtestingAutoW;


    //private final JwtUtil jwtUtil;

    //private final accountRepo accountRepo;

    @GetMapping("/home/kosdaq")
    public List<KosdaqResponseDTO> getKosdakYearData(){
        return kosdaqService.getResponsesByYear();
    }


    @GetMapping("/home/kospi")
    public List<KospiResponseDTO> getKospiYearData(){
        return kospiService.getResponsesByYear();
    }

    @GetMapping("/home/kospi200")
    public List<Kospi200ResponseDTO> getKospi200YearData(){
        return kospi200Service.getResponsesByYear();
    }



    @GetMapping("/home/top20")
    public List<SchedulingCoinDTO> getTop20Coin(){
        return coinService.getTop20CoinByClosingPrice();
    }

    @GetMapping("/home/coinByFluctuating")
    public List<SchedulingCoinDTO> getCoinByFluctuating(){
        return coinService.getCoinByFluctuating();
    }

    @GetMapping("/home/coinByClosingPrice")
    public List<SchedulingCoinDTO> getCoinByClosingPrice(){
        return coinService.getCoinByClosingPrice();
    }

    @GetMapping("/home/coinByTradingVolume")
    public List<SchedulingCoinDTO> getCoinByTradingVolume(){
        return coinService.getCoinByTradingVolume();
    }

    // 시스템 백테스팅 기록을 가져오는 API
    @GetMapping("/home/backtesting_gd")
    public List<GDEntity> getBacktestingResultsGD() {
        backtestingAutoGD.runAutomaticBacktesting(10,1, 0L);
        return strategyService.getRecent100GDStrategies((long) -1);
    }
    @GetMapping("/home/backtesting_bb")
    public List<BBEntity> getBacktestingResultsBB() {
        backtestingAutoBB.runAutomaticBacktesting(10,1,0L);
        return strategyService.getRecent100BBStrategies((long) -2);
    }
    @GetMapping("/home/backtesting_ind")
    public List<IndicatorEntity> getBacktestingResultsInd() {
        backtestingAutoInd.runAutomaticBacktesting(10,1,0L);
        return strategyService.getRecent100IndStrategies((long) -3);
    }
    @GetMapping("/home/backtesting_env")
    public List<EnvEntity> getBacktestingResultsEnv() {
        backtestingAutoEnv.runAutomaticBacktesting(10,1,0L);
        return strategyService.getRecent100EnvStrategies((long) -4);
    }
    @GetMapping("/home/backtesting_w")
    public List<WEntity> getBacktestingResultsW() {
        backtestingAutoW.runAutomaticBacktesting(10,1,0L);
        return strategyService.getRecent100WStrategies((long) -5);
    }
//    @GetMapping("/home/coin/{market}")
//    public String getMarketData(@PathVariable("market") String market){
//        final OkHttpClient client = new OkHttpClient();
//        String currentPrice = "https://api.bithumb.com/v1/ticker";
//        String urlWithMarket = currentPrice + "?markets=" + market;
//        Request priceRequest = new Request.Builder()
//                .url(urlWithMarket)
//                .get()
//                .addHeader("accept", "application/json")
//                .build();
//        try (Response priceResponse = client.newCall(priceRequest).execute()) {
//            // API 응답을 JSON 형식 그대로 반환
//            return priceResponse.body().string();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "{\"error\": \"API 요청에 실패하였습니다.\"}";
//        }
//
//    }
    @GetMapping("/home/coin/{market}")
    public List<CoinDetailsDTO> getCandles(@PathVariable("market") String market) {
        final OkHttpClient client = new OkHttpClient();
        String candleUrl = "https://api.bithumb.com/v1/candles/days";
        List<CoinDetailsDTO> coinDetailsList = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        // URL을 구성하여 요청
        String urlWithParams = candleUrl + "?market=" + market + "&count=200";
        Request candleRequest = new Request.Builder()
                .url(urlWithParams)
                .get()
                .addHeader("accept", "application/json")
                .build();

        try (Response candleResponse = client.newCall(candleRequest).execute()) {
            // 응답 데이터를 JSON 형식으로 반환
            String jsonData = candleResponse.body().string();

            JsonNode rootNode = objectMapper.readTree(jsonData);
            for(JsonNode node : rootNode){
                CoinDetailsDTO coinDetails = new CoinDetailsDTO();
                coinDetails.setMarket(node.get("market").asText());

                String candleDateKST = node.get("candle_date_time_kst").asText();
                LocalDate date = LocalDate.parse(candleDateKST.substring(0, 10));  // yyyy-MM-dd 형태로 변환
                coinDetails.setDate(date);

                coinDetails.setClosingPrice(node.get("trade_price").asDouble()); // 종가
                coinDetails.setOpeningPrice(node.get("opening_price").asDouble()); // 시가
                coinDetails.setHighPrice(node.get("high_price").asDouble()); // 고가
                coinDetails.setLowPrice(node.get("low_price").asDouble()); // 저가
                coinDetails.setTradingVolume(node.get("candle_acc_trade_volume").asText()); // 거래량
                coinDetails.setFluctuatingRate(node.get("change_rate").asText()); // 변화율
                coinDetailsList.add(coinDetails);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return coinDetailsList;
    }
    /*

    @GetMapping("/userinfo")
    public ResponseEntity<?> getUserInfo(@RequestHeader("Authorization") String token){
        Long userId = jwtUtil.getUserIdFromToken(token);
        log.info("userId::{}", userId);

        Optional<Account> byId = accountRepo.findById(userId);


        UserDTO userDTO = new UserDTO(byId.get().getUsername(), byId.get().getEmail());

        String name = byId.get().getUsername();
        String email = byId.get().getEmail();

        log.info("name: {}, email: {}", name, email);

        return ResponseEntity.ok(userDTO);
    }
*/

    /*
    @GetMapping("/user/info")
    public ResponseEntity<UserDTO> getMyPage(@RequestHeader("Authorization") String token){
        Long userId = jwtUtil.getUserIdFromToken(token);
        Optional<Account> byId = accountRepo.findById(userId);


        UserDTO userDTO = new UserDTO(byId.get().getUsername(), byId.get().getEmail());

        String name = byId.get().getUsername();
        String email = byId.get().getEmail();

        log.info("name: {}, email: {}", name, email);

        return ResponseEntity.ok(userDTO);
    }
    */

}
