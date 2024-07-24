package com.example.BeFETest.controller;

import com.example.BeFETest.BusinessLogicLayer.Strategy.StrategyService;
import com.example.BeFETest.DTO.coinDTO.*;
import com.example.BeFETest.DTO.kosdak.KosdakConverter;
import com.example.BeFETest.DTO.kosdak.KosdakResponseDTO;
import com.example.BeFETest.DTO.kosdak2000.Kosdak2000Converter;
import com.example.BeFETest.DTO.kosdak2000.Kosdak2000ResponseDTO;
import com.example.BeFETest.DTO.kospi.KospiConverter;
import com.example.BeFETest.DTO.kospi.KospiResponseDTO;
import com.example.BeFETest.Entity.BacktestingRes.GDEntity;
import com.example.BeFETest.Entity.UserEntity;
import com.example.BeFETest.Entity.UserInfo;
import com.example.BeFETest.Entity.UserRequest;
import com.example.BeFETest.Entity.kosdak.KosdakResponse;
import com.example.BeFETest.Entity.kosdak2000.Kosdak2000Response;
import com.example.BeFETest.Entity.kospi.KospiResponse;
import com.example.BeFETest.Error.CustomExceptions;

import com.example.BeFETest.Error.ErrorCode;

import com.example.BeFETest.JWT.JwtUtil;
import com.example.BeFETest.Repository.Backtesting.BacktestingHistoryRepository;
import com.example.BeFETest.Repository.JWT.UserRepository;
import com.example.BeFETest.Repository.Kosdak.Kosdak2000Repository;
import com.example.BeFETest.Repository.Kosdak.KosdakRepository;
import com.example.BeFETest.Repository.Kospi.KospiRepository;
import com.example.BeFETest.Strategy.BacktestingGD;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@RestController
@RequiredArgsConstructor
public class testController {

    @Autowired
    private final BacktestingHistoryRepository backtestingHistoryRepository;

    @Autowired
    private final KospiRepository kospiRepository;

    @Autowired
    private final Kosdak2000Repository kosdak2000Repository;

    @Autowired
    private final KosdakRepository kosdakRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private StrategyService strategyService;

    private  StrategyService gdService;


    @PostMapping("/mypage")
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

    @GetMapping("/home/kosdak")
    public ResponseEntity<KosdakResponseDTO> getKosdak(){
        try {
            LocalDate currentDate = LocalDate.now();
            String currentDateString = currentDate.toString();
            KosdakResponse kosdakResponse = kosdakRepository.findByDate(currentDateString);
            if(kosdakResponse != null){
                KosdakResponseDTO kosdakDTO = KosdakConverter.toDto(kosdakResponse);
                return new ResponseEntity<>(kosdakDTO, HttpStatus.OK);
            } else {
                throw new CustomExceptions.ResourceNotFoundException("Resource Not", null, "Resource Not", ErrorCode.NOT_FOUND);
            }
        } catch (CustomExceptions.BadGatewayException e){//(CustomExceptions.ResourceNotFoundException e) {
            throw e; // ResourceNotFoundException 그대로 던짐
        } catch (Exception e) {
            throw new CustomExceptions.InternalServerErrorException("Internal Error", e, "Internal Error", ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/home/kospi")
    public ResponseEntity<KospiResponseDTO> getKospi(){
        try {
            LocalDate currentDate = LocalDate.now();
            String currentDateString = currentDate.toString();
            KospiResponse kospiResponse = kospiRepository.findByDate(currentDateString);
            if(kospiResponse != null){
                KospiResponseDTO kospiDTO = KospiConverter.toDto(kospiResponse);
                return new ResponseEntity<>(kospiDTO, HttpStatus.OK);
            } else {
                throw new CustomExceptions.ResourceNotFoundException("Resource Not", null, "Resource Not", ErrorCode.NOT_FOUND);
            }
        } catch (CustomExceptions.ResourceNotFoundException e) {
            throw e; // ResourceNotFoundException 그대로
        } catch (Exception e) {
            throw new CustomExceptions.InternalServerErrorException("Internal Error", e, "Internal Error", ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    
    @GetMapping("/home/kosdak2000")
    public ResponseEntity<Kosdak2000ResponseDTO> getKosdak2000(){
        try {
            LocalDate currentDate = LocalDate.now();
            String currentDateString = currentDate.toString();
            Kosdak2000Response kosdak2000Response = kosdak2000Repository.findByDate(currentDateString);
            if(kosdak2000Response != null){
                Kosdak2000ResponseDTO kosdak2000DTO = Kosdak2000Converter.toDto(kosdak2000Response);
                return new ResponseEntity<>(kosdak2000DTO, HttpStatus.OK);
            } else {
                throw new CustomExceptions.ResourceNotFoundException("Resource Not", null, "Resource Not", ErrorCode.NOT_FOUND);
            }
        } catch (Exception e){
            throw new CustomExceptions.InternalServerErrorException("Internal Error", null, "Internal Error", ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/strategy")
    public ResponseEntity<?> saveCommonStrategy(@RequestHeader("Authorization") String token, @RequestBody StrategyCommonDTO strategyCommonDTO){

        Long userId = jwtUtil.getUserIdFromToken(token);
        System.out.println("userId = " + userId);
        strategyService.saveGDCommonStrategyResult(strategyCommonDTO, userId);
        return ResponseEntity.ok("Common strategy saved successfully");
    }

    @PostMapping("/strategy/golden")
    public ResponseEntity<?> saveGDStrategy(@RequestHeader("Authorization") String token, @RequestBody GoldenDeadCrossStrategyDTO gdStrategyDTO){

        Long userId = jwtUtil.getUserIdFromToken(token);
        gdStrategyDTO.setUserId(userId);
        strategyService.saveGDStrategyResult(gdStrategyDTO);
        System.out.println("userId = " + userId);
        System.out.println("GD = " + gdStrategyDTO.toString());
        return ResponseEntity.ok("GD strategy saved successfully");
        //List<GDEntity> strategies = strategyService.getRecentGDStrategies(userId);
        //for (GDEntity strategy : strategies) {
        //    System.out.println("strategy = " + strategy.toString());
        //}
        //return ResponseEntity.ok(strategies);
    }

    @PostMapping("/strategy/bollinger")
    public ResponseEntity<?> saveBBStrategy(@RequestHeader("Authorization") String token, @RequestBody BollingerBandsStrategyDTO bbStrategyDTO){

        Long userId = jwtUtil.getUserIdFromToken(token);
        bbStrategyDTO.setUserId(userId);
        strategyService.saveBBStrategyResult(bbStrategyDTO);
        System.out.println("userId = " + userId);
        System.out.println("BB = " + bbStrategyDTO.toString());
        return ResponseEntity.ok("BB strategy saved successfully");
    }

    @PostMapping("/strategy/rsi")
    public ResponseEntity<?> saveRSIStrategy(@RequestHeader("Authorization") String token, @RequestBody IndicatorBasedStrategyDTO indicatorDTO){

        Long userId = jwtUtil.getUserIdFromToken(token);
        indicatorDTO.setUserId(userId);
        strategyService.saveIndicatorStrategyResult(indicatorDTO);
        System.out.println("userId = " + userId);
        System.out.println("Indicator = " + indicatorDTO.toString());
        return ResponseEntity.ok("Indicator strategy saved successfully");
    }



    //@GetMapping("/strategy/golden/result")
    @GetMapping("/result")
    public ResponseEntity<?> getGDStrategyResult(@RequestHeader("Authorization") String token){
        System.out.println("GD Strategy Result -> ");

        try{
            GoldenDeadCrossStrategyDTO goldenDeadCrossStrategyDTO = BacktestingGD.executeTrades();
            gdService.saveGDStrategyResult(goldenDeadCrossStrategyDTO);
            return ResponseEntity.ok(goldenDeadCrossStrategyDTO);
//            Long userId = jwtUtil.getUserIdFromToken(token);
//            GoldenDeadCrossStrategyDTO gdResultDTO = strategyService.getLatestGDStrategyResultByUserId(userId);
//            return ResponseEntity.ok(gdResultDTO);

        } catch(CustomExceptions.ResourceNotFoundException e){
            throw e;
        } catch(Exception e){
            throw new CustomExceptions.InternalServerErrorException("Internal Error", e, "Internal Error", ErrorCode.INTERNAL_SERVER_ERROR);
        }
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
