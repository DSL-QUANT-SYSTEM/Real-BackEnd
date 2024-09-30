package com.example.BeFETest.controller;

import com.example.BeFETest.BusinessLogicLayer.BacktestingAuto.*;
import com.example.BeFETest.BusinessLogicLayer.Strategy.StrategyService;
import com.example.BeFETest.BusinessLogicLayer.coin.CoinService;
import com.example.BeFETest.BusinessLogicLayer.kosdaq.KosdaqResponseService;
import com.example.BeFETest.BusinessLogicLayer.kospi.Kospi200ResponseService;
import com.example.BeFETest.BusinessLogicLayer.kospi.KospiResponseService;
import com.example.BeFETest.DTO.SchedulingCoin.SchedulingCoinDTO;
import com.example.BeFETest.DTO.kosdaq.KosdaqResponseDTO;
import com.example.BeFETest.DTO.kospi.KospiResponseDTO;
import com.example.BeFETest.DTO.kospi200.Kospi200ResponseDTO;
import com.example.BeFETest.Entity.BacktestingRes.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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



    // 백테스팅 기록을 가져오는 API
    @GetMapping("/home/backtesting_gd")
    public List<GDEntity> getBacktestingResultsGD() {
        backtestingAutoGD.runAutomaticBacktesting(10);
        return strategyService.getRecent100GDStrategies((long) -1);
    }
    @GetMapping("/home/backtesting_bb")
    public List<BBEntity> getBacktestingResultsBB() {
        backtestingAutoBB.runAutomaticBacktesting(10);
        return strategyService.getRecent100BBStrategies((long) -2);
    }
    @GetMapping("/home/backtesting_ind")
    public List<IndicatorEntity> getBacktestingResultsInd() {
        backtestingAutoInd.runAutomaticBacktesting(10);
        return strategyService.getRecent100IndStrategies((long) -3);
    }
    @GetMapping("/home/backtesting_env")
    public List<EnvEntity> getBacktestingResultsEnv() {
        backtestingAutoEnv.runAutomaticBacktesting(10);
        return strategyService.getRecent100EnvStrategies((long) -4);
    }
    @GetMapping("/home/backtesting_w")
    public List<WEntity> getBacktestingResultsW() {
        backtestingAutoW.runAutomaticBacktesting(10);
        return strategyService.getRecent100WStrategies((long) -5);
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
