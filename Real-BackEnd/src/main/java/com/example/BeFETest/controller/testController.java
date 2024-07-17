package com.example.BeFETest.controller;

import com.example.BeFETest.DTO.coinDTO.GoldenDeadCrossStrategyDTO;
import com.example.BeFETest.DTO.coinDTO.StrategyCommonDTO;
import com.example.BeFETest.DTO.kosdak.KosdakConverter;
import com.example.BeFETest.DTO.kosdak.KosdakResponseDTO;
import com.example.BeFETest.DTO.kosdak2000.Kosdak2000Converter;
import com.example.BeFETest.DTO.kosdak2000.Kosdak2000ResponseDTO;
import com.example.BeFETest.DTO.kospi.KospiConverter;
import com.example.BeFETest.DTO.kospi.KospiResponseDTO;
import com.example.BeFETest.Entity.UserEntity;
import com.example.BeFETest.Entity.UserInfo;
import com.example.BeFETest.Entity.UserRequest;
import com.example.BeFETest.Entity.coinStrategy.GoldenDeadCrossStrategyEntity;
import com.example.BeFETest.Entity.kosdak.KosdakEntity;
import com.example.BeFETest.Entity.kosdak.KosdakResponse;
import com.example.BeFETest.Entity.kosdak2000.Kosdak2000Response;
import com.example.BeFETest.Entity.kospi.KospiResponse;
import com.example.BeFETest.Error.CustomExceptions;

import com.example.BeFETest.Error.ErrorCode;
import com.example.BeFETest.JWT.JwtUtil;
import com.example.BeFETest.Repository.*;

import com.example.BeFETest.service.StrategyService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    //private StrategyCommonDTO strategyCommonDTO;
    //private GoldenDeadCrossStrategyDTO goldenDTO;

    private StrategyCommonDTO strategyCommonDTO;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private StrategyService strategyService;

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
    /*
    public ResponseEntity<KosdakResponseDTO> getKosdak() {              // 테스트용 임의 데이터 생성 코드
        try {
            KosdakResponse kosdakResponse = new KosdakResponse();
            kosdakResponse.setId(1L);
            kosdakResponse.setDate(LocalDate.now().toString());
            kosdakResponse.setCurrentPrice(737000);
            kosdakResponse.setAllDayRatio(2.2);
            kosdakResponse.setPercentChange(-3.5);

            KosdakEntity kosdakEntity = new KosdakEntity();
            kosdakEntity.setId(1L);
            kosdakEntity.setTime(LocalDateTime.now().toString());
            kosdakEntity.setValue(737000);
            kosdakEntity.setResponse(kosdakResponse);

            List<KosdakEntity> kosdakEntities = new ArrayList<>();
            kosdakEntities.add(kosdakEntity);
            kosdakResponse.setKosdakData(kosdakEntities);

            KosdakResponseDTO kosdakResponseDTO = KosdakConverter.toDto(kosdakResponse);

            return new ResponseEntity<>(kosdakResponseDTO, HttpStatus.OK);
        } catch (Exception e) {
            throw new CustomExceptions.InternalServerErrorException();
        }
    }
    */
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

    @GetMapping("/fortest")
    public ResponseEntity<?> fortestfunc(){
        //throw new RuntimeException("Test exception for AOP logging");
        throw new CustomExceptions.ForbiddenException("Forbid", null, "Forbidd", ErrorCode.FORBIDDEN);
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

    /*
    @PostMapping("/strategy")
    public ResponseEntity<?> saveCommonStrategy(@RequestBody StrategyCommonDTO strategyCommonDTO){
        this.strategyCommonDTO = strategyCommonDTO;
        return ResponseEntity.ok("Common strategy settings saved successfully");
    }

    @PostMapping("/strategy/golden")
    public ResponseEntity<?> saveGoldenDeadCrossStrategy(@RequestBody GoldenDeadCrossStrategyDTO goldenDeadCrossStrategyDTO) {

        if (strategyCommonDTO != null) {
            // Combine commonDTO with goldenDeadCrossStrategyDTO
            goldenDTO = new GoldenDeadCrossStrategyDTO(
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
            System.out.println("Combined Strategy: " + goldenDTO.toString());
            // Return the combinedDTO
            return ResponseEntity.ok(goldenDTO);
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


    private Long extractUserIdFromToken(String token) {
        return jwtUtil.extractUserIdFromToken(token);
    }

    @PostMapping("/")
    public ResponseEntity<?> saveCommonStrategy(@RequestBody StrategyCommonDTO strategyCommonDTO){
        this.strategyCommonDTO = strategyCommonDTO;
        System.out.println(" = !!!123123123!" );
        return ResponseEntity.ok("Common strategy settings saved successfully");
    }


    @PostMapping("/strategy/golden")
    public ResponseEntity<?> saveGoldenDeadCrossStrategy(@RequestBody GoldenDeadCrossStrategyDTO strategy, @RequestHeader("Authorization") String token) {
        Long userId = extractUserIdFromToken(token);

        System.out.println(" = !!!???!" );


        if (strategyCommonDTO != null) {
            // Combine commonDTO with goldenDeadCrossStrategyDTO
            GoldenDeadCrossStrategyDTO goldenDeadCrossStrategyDTO = new GoldenDeadCrossStrategyDTO(
                    strategyCommonDTO.getInitialInvestment(),
                    strategyCommonDTO.getTransactionFee(),
                    strategyCommonDTO.getStartDate(),
                    strategyCommonDTO.getEndDate(),
                    strategyCommonDTO.getTargetItem(),
                    strategyCommonDTO.getTickKind(),
                    strategyCommonDTO.getInquiryRange(),
                    strategy.getFastMovingAveragePeriod(),
                    strategy.getSlowMovingAveragePeriod()
            );

            // Here you can save or process combinedDTO as needed
            System.out.println("Combined Strategy: " + goldenDeadCrossStrategyDTO.toString());

            GoldenDeadCrossStrategyEntity goldenEntity = new GoldenDeadCrossStrategyEntity();
            goldenEntity.setInitialInvestment(goldenDeadCrossStrategyDTO.getInitialInvestment());
            goldenEntity.setTransactionFee(goldenDeadCrossStrategyDTO.getTransactionFee());
            goldenEntity.setStartDate(goldenDeadCrossStrategyDTO.getStartDate());
            goldenEntity.setEndDate(goldenDeadCrossStrategyDTO.getEndDate());
            goldenEntity.setTargetItem(goldenDeadCrossStrategyDTO.getTargetItem());
            goldenEntity.setTickKind(goldenDeadCrossStrategyDTO.getTickKind());
            goldenEntity.setInquiryRange(goldenDeadCrossStrategyDTO.getInquiryRange());
            goldenEntity.setFastMovingAveragePeriod(goldenDeadCrossStrategyDTO.getFastMovingAveragePeriod());
            goldenEntity.setSlowMovingAveragePeriod(goldenDeadCrossStrategyDTO.getSlowMovingAveragePeriod());

            goldenEntity.setUserId(userId);
            strategyService.saveGoldenDeadCrossStrategy(goldenEntity);

            System.out.println(" = !!!!" );

            return ResponseEntity.ok("Golden/Dead Cross strategy settings saved successfully");
        }else{
            return ResponseEntity.badRequest().body("Common strategy settings not set");
        }
    }


    @GetMapping("/result")
    public ResponseEntity<List<GoldenDeadCrossStrategyEntity>> getGoldenDeadCrossStrategy(@RequestHeader("Authorization") String token) {
        Long userId = extractUserIdFromToken(token);
        List<GoldenDeadCrossStrategyEntity> strategies = strategyService.getRecentGoldenDeadCrossStrategies(userId);
        return ResponseEntity.ok(strategies);
    }
}
