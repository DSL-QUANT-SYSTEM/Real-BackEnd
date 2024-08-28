package com.example.BeFETest.controller;

import com.example.BeFETest.BusinessLogicLayer.Strategy.StrategyService;
import com.example.BeFETest.DTO.coinDTO.*;
import com.example.BeFETest.Entity.UserEntity;
import com.example.BeFETest.Entity.UserInfo;
import com.example.BeFETest.Entity.UserRequest;
import com.example.BeFETest.Error.CustomExceptions;

import com.example.BeFETest.Error.ErrorCode;

import com.example.BeFETest.JWT.JwtUtil;
import com.example.BeFETest.Repository.Backtesting.BacktestingHistoryRepository;
import com.example.BeFETest.Repository.JWT.UserRepository;
import com.example.BeFETest.Repository.Kospi.Kospi200Repository;
import com.example.BeFETest.Repository.Kosdaq.KosdaqRepository;
import com.example.BeFETest.Repository.Kospi.KospiRepository;
import com.example.BeFETest.Strategy.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class testController {

    @Autowired
    private final BacktestingHistoryRepository backtestingHistoryRepository;

    @Autowired
    private final KospiRepository kospiRepository;

    @Autowired
    private final Kospi200Repository kospi200Repository;

    @Autowired
    private final KosdaqRepository kosdaqRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private StrategyService strategyService;


    private StrategyCommonDTO commonDTO;

    //@PostMapping("/mypage")
    public UserInfo checkUserInfo(@RequestBody UserRequest request) {
        UserEntity userEntity = userRepository.findByBirthDate(request.getUser_birth());
        if(userEntity == null) {
            throw new CustomExceptions.ResourceNotFoundException("Resource Not found", null, "Resource Not found", ErrorCode.NOT_FOUND);
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setName(userEntity.getName());
        userInfo.setPhone(userEntity.getPhoneNumber());
        userInfo.setBirthDate(userEntity.getBirthDate());
        userInfo.setGender(userEntity.getGender());

        // BacktestingHistory 객체를 문자열 배열로 변환
        String[] records = userEntity.getRecords().stream()
                .map(record -> "Date: " + record.getDate() +
                        ", Universe: " + record.getUniverse() +
                        ", weight: " + record.getWeight() +
                        ", Initial Investment: " + record.getInitialInvestment() +
                        ", Period: " + record.getPeriod() +
                        ", File HTML: " + record.getFileHtml())
                .toArray(String[]::new);

        userInfo.setBacktestingRecords(records);

        return userInfo;
    }

    //@GetMapping("/home/kosdak")
//    public ResponseEntity<KosdakResponseDTO> getKosdak(){
//        try {
//            LocalDate currentDate = LocalDate.now();
//            String currentDateString = currentDate.toString();
//            KosdakResponse kosdakResponse = kosdakRepository.findByDate(currentDateString);
//            if(kosdakResponse != null){
//                KosdakResponseDTO kosdakDTO = KosdakConverter.toDto(kosdakResponse);
//                return new ResponseEntity<>(kosdakDTO, HttpStatus.OK);
//            } else {
//                throw new CustomExceptions.ResourceNotFoundException("Resource Not", null, "Resource Not", ErrorCode.NOT_FOUND);
//            }
//        } catch (CustomExceptions.BadGatewayException e){//(CustomExceptions.ResourceNotFoundException e) {
//            throw e; // ResourceNotFoundException 그대로 던짐
//        } catch (Exception e) {
//            throw new CustomExceptions.InternalServerErrorException("Internal Error" + e.getMessage(), e, "Internal Error" + e.getMessage(), ErrorCode.INTERNAL_SERVER_ERROR);
//        }
//    }

    //@GetMapping("/home/kospi")
//    public ResponseEntity<KospiResponseDTO> getKospi(){
//        try {
//            LocalDate currentDate = LocalDate.now();
//            String currentDateString = currentDate.toString();
//            KospiResponse kospiResponse = kospiRepository.findByDate(currentDateString);
//            if(kospiResponse != null){
//                KospiResponseDTO kospiDTO = KospiConverter.toDto(kospiResponse);
//                return new ResponseEntity<>(kospiDTO, HttpStatus.OK);
//            } else {
//                throw new CustomExceptions.ResourceNotFoundException("Resource Not", null, "Resource Not", ErrorCode.NOT_FOUND);
//            }
//        } catch (CustomExceptions.ResourceNotFoundException e) {
//            throw e; // ResourceNotFoundException 그대로
//        } catch (Exception e) {
//            throw new CustomExceptions.InternalServerErrorException("Internal Error", e, "Internal Error", ErrorCode.INTERNAL_SERVER_ERROR);
//        }
//    }


    //@GetMapping("/home/kospi200")
//    public ResponseEntity<Kospi200ResponseDTO> getkospi200(){
//        try {
//            LocalDate currentDate = LocalDate.now();
//            String currentDateString = currentDate.toString();
//            Kospi200Response kospi200Response = kospi200Repository.findByDate(currentDateString);
//            if(kospi200Response != null){
//                Kospi200ResponseDTO kospi200DTO = Kospi200Converter.toDto(kospi200Response);
//                return new ResponseEntity<>(kospi200DTO, HttpStatus.OK);
//            } else {
//                throw new CustomExceptions.ResourceNotFoundException("Resource Not", null, "Resource Not", ErrorCode.NOT_FOUND);
//            }
//        } catch (Exception e){
//            throw new CustomExceptions.InternalServerErrorException("Internal Error", null, "Internal Error", ErrorCode.INTERNAL_SERVER_ERROR);
//        }
//    }

    @PostMapping("/strategy")
    public ResponseEntity<?> saveCommonStrategy(@RequestHeader("Authorization")  @RequestBody StrategyCommonDTO strategyCommonDTO){
        commonDTO=strategyCommonDTO;
        return ResponseEntity.ok("Common strategy saved successfully");
    }

    @PostMapping("/strategy/golden")
    public ResponseEntity<?> saveGDStrategy(@RequestHeader("Authorization") String token, @RequestBody GoldenDeadCrossStrategyDTO gdStrategyDTO){

        System.out.println("FOR TEST");
        System.out.println("commonDTO = " + commonDTO.toString());
        System.out.println("fastMoveAvg = " + gdStrategyDTO.getFastMoveAvg() + ", slowMovAvg = " + gdStrategyDTO.getSlowMoveAvg());
        Long userId = jwtUtil.getUserIdFromToken(token);
        gdStrategyDTO.setUserId(userId);
        System.out.println("userId = " + userId);
        GoldenDeadCrossStrategyDTO gdResultDTO = BacktestingGD.executeTrades(commonDTO, gdStrategyDTO);
        strategyService.saveGDStrategyResult(commonDTO, userId, gdStrategyDTO,gdResultDTO);
        //디비 저장과 프론트 표시간에 문제점발생

        return ResponseEntity.ok("GD strategy saved successfully");
        //List<GDEntity> strategies = strategyService.getRecentGDStrategies(userId);
        //for (GDEntity strategy : strategies) {
        //    System.out.println("strategy = " + strategy.toString());
        //}
        //return ResponseEntity.ok(strategies);
    }

    @PostMapping("/strategy/bollinger")
    public ResponseEntity<?> saveBBStrategy(@RequestHeader("Authorization") String token, @RequestBody BollingerBandsStrategyDTO bbStrategyDTO){

        System.out.println("FOR TEST");
        System.out.println("commonDTO = " + commonDTO.toString());
        System.out.println("MovingAveragePeriod= "+ bbStrategyDTO.getMoveAvg());
        Long userId = jwtUtil.getUserIdFromToken(token);
        bbStrategyDTO.setUserId(userId);
        System.out.println("userId = " + userId);
        BollingerBandsStrategyDTO bbResultDTO = BacktestingBB.executeTrades(commonDTO, bbStrategyDTO);
        strategyService.saveBBStrategyResult(commonDTO, userId, bbStrategyDTO,bbResultDTO);
        return ResponseEntity.ok("BB strategy saved successfully");
    }

    @PostMapping("/strategy/rsi")
    public ResponseEntity<?> saveRSIStrategy(@RequestHeader("Authorization") String token, @RequestBody IndicatorBasedStrategyDTO indicatorDTO){

        System.out.println("FOR TEST");
        System.out.println("commonDTO = " + commonDTO.toString());
        System.out.println("rsiPeriod= "+ indicatorDTO.getRsiPeriod());
        Long userId = jwtUtil.getUserIdFromToken(token);
        indicatorDTO.setUserId(userId);
        System.out.println("userId = " + userId);
        IndicatorBasedStrategyDTO indResultDTO = BacktestingIndicator.executeTrades(commonDTO, indicatorDTO);
        strategyService.saveIndicatorStrategyResult(commonDTO, userId, indicatorDTO, indResultDTO);
        return ResponseEntity.ok("Indicator strategy saved successfully");
    }

    @PostMapping("/strategy/env")
    public ResponseEntity<?> saveEnvStrategy(@RequestHeader("Authorization") String token, @RequestBody EnvelopeDTO envelopeDTO){

        System.out.println("FOR TEST");
        System.out.println("commonDTO = " + commonDTO.toString());
        System.out.println("Moving_up= "+ envelopeDTO.getMoving_up());
        System.out.println("Moving_down= "+ envelopeDTO.getMoving_down());
        System.out.println("MovingAveragePeriod= "+ envelopeDTO.getMovingAveragePeriod());
        Long userId = jwtUtil.getUserIdFromToken(token);
        envelopeDTO.setUserId(userId);
        System.out.println("userId = " + userId);
        EnvelopeDTO envResultDTO = BacktestingEnv.executeTrades(commonDTO, envelopeDTO);
        strategyService.saveEnvelopeStrategyResult(commonDTO, userId, envelopeDTO, envResultDTO);
        return ResponseEntity.ok("Indicator strategy saved successfully");
    }

    @PostMapping("/strategy/williams")
    public ResponseEntity<?> saveWStrategy(@RequestHeader("Authorization") String token, @RequestBody WilliamsDTO williamsDTO){

        System.out.println("FOR TEST");
        System.out.println("commonDTO = " + commonDTO.toString());
        System.out.println("WilliamsPeriod= "+ williamsDTO.getWilliamsPeriod());
        Long userId = jwtUtil.getUserIdFromToken(token);
        williamsDTO.setUserId(userId);
        System.out.println("userId = " + userId);
        WilliamsDTO williamsResultDTO = BacktestingW.executeTrades(commonDTO, williamsDTO);
        strategyService.saveWilliamsStrategyResult(commonDTO, userId, williamsDTO, williamsResultDTO);
        return ResponseEntity.ok("Indicator strategy saved successfully");
    }




    @GetMapping("/result/golden")
    public ResponseEntity<?> getGDStrategyResult(@RequestHeader("Authorization") String token){
        System.out.println(commonDTO.getStrategy()+ "Backtesting Result -> ");
        Long userId = jwtUtil.getUserIdFromToken(token);
        GoldenDeadCrossStrategyDTO gdResultDTO = strategyService.getLatestGDStrategyResultByUserId(userId);
        return ResponseEntity.ok(gdResultDTO);
    }
    @GetMapping("/result/bollinger")
    public ResponseEntity<?> getBBStrategyResult(@RequestHeader("Authorization") String token){
        System.out.println(commonDTO.getStrategy()+ "Backtesting Result -> ");
        Long userId = jwtUtil.getUserIdFromToken(token);
        BollingerBandsStrategyDTO bbResultDTO = strategyService.getLatestBBStrategyResultByUserId(userId);
        return ResponseEntity.ok(bbResultDTO);
    }
    @GetMapping("/result/rsi")
    public ResponseEntity<?> getIndicatorStrategyResult(@RequestHeader("Authorization") String token){
        System.out.println(commonDTO.getStrategy()+ "Backtesting Result -> ");
        Long userId = jwtUtil.getUserIdFromToken(token);
        IndicatorBasedStrategyDTO indResultDTO = strategyService.getLatestIndicatorStrategyResultByUserId(userId);
        return ResponseEntity.ok(indResultDTO);
    }
    @GetMapping("/result/env")
    public ResponseEntity<?> getEnvStrategyResult(@RequestHeader("Authorization") String token){
        System.out.println(commonDTO.getStrategy()+ "Backtesting Result -> ");
        Long userId = jwtUtil.getUserIdFromToken(token);
        EnvelopeDTO envelopeDTO = strategyService.getLatestEnvStrategyResultByUserId(userId);
        return ResponseEntity.ok(envelopeDTO);
    }
    @GetMapping("/result/williams")
    public ResponseEntity<?> getWilliamsStrategyResult(@RequestHeader("Authorization") String token){
        System.out.println(commonDTO.getStrategy()+ "Backtesting Result -> ");
        Long userId = jwtUtil.getUserIdFromToken(token);
        WilliamsDTO williamsDTO = strategyService.getLatestWilliamsStrategyResultByUserId(userId);
        return ResponseEntity.ok(williamsDTO);
    }

    /*
    @PostMapping("/strategy")
    public ResponseEntity<?> saveCommonStrategy(@RequestBody StrategyCommonDTO strategyCommonDTO){
        this.strategyCommonDTO = strategyCommonDTO;
        return ResponseEntity.ok("Common strategy settings saved successfully");
    }

    /*
    @PostMapping("/strategy/golden")
    public ResponseEntity<?> saveGoldenDeadCrossStrategy(@RequestBody GoldenDeadCrossStrategyDTO goldenDeadCrossStrategyDTO) {

        if (strategyCommonDTO != null) {
            // Combine commonDTO with goldenDeadCrossStrategyDTO
            combinedDTO = new GoldenDeadCrossStrategyDTO(
                    strategyCommonDTO.getInitialInvestment(),
                    strategyCommonDTO.getTransactionFee(),
                    strategyCommonDTO.getStartDate(),
                    strategyCommonDTO.getEndDate(),
                    strategyCommonDTO.getTargetItem(),
                    strategyCommonDTO.getTickKind(),
                    strategyCommonDTO.getInquiryRange(),
                    goldenDeadCrossStrategyDTO.getFastMovingAveragePeriod(),
                    goldenDeadCrossStrategyDTO.getSlowMovingAveragePeriod()
            );

            // Here you can save or process combinedDTO as needed
            System.out.println("Combined Strategy: " + combinedDTO.toString());

            // Return the combinedDTO
            return ResponseEntity.ok(combinedDTO);
        } else {
            return ResponseEntity.badRequest().body("Common strategy settings not set");
        }
    }
    */


    /*
    @PostMapping("/strategy/golden")
    public ResponseEntity<?> saveGoldenDeadCrossStrategy(@RequestBody GoldenDeadCrossStrategyDTO goldenDeadCrossStrategyDTO) {
        if (strategyCommonDTO != null) {
            // Combine commonDTO with goldenDeadCrossStrategyDTO
            GoldenDeadCrossStrategyDTO combinedDTO = new GoldenDeadCrossStrategyDTO(
                    strategyCommonDTO.getInitialInvestment(),
                    strategyCommonDTO.getTransactionFee(),
                    strategyCommonDTO.getStartDate(),
                    strategyCommonDTO.getEndDate(),
                    strategyCommonDTO.getTargetItem(),
                    strategyCommonDTO.getTickKind(),
                    strategyCommonDTO.getInquiryRange(),
                    goldenDeadCrossStrategyDTO.getFastMovingAveragePeriod(),
                    goldenDeadCrossStrategyDTO.getSlowMovingAveragePeriod()
            );

            // Here you can save or process combinedDTO as needed
            System.out.println("Combined Strategy: " + combinedDTO.toString());
        } else {
            return ResponseEntity.badRequest().body("Common strategy settings not set");
        }
        return ResponseEntity.ok("Golden/Dead Cross strategy settings saved successfully");
    }
    */
}
