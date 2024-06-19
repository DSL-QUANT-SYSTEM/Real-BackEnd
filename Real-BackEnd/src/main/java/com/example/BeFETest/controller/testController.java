package com.example.BeFETest.controller;

import com.example.BeFETest.Entity.UserEntity;
import com.example.BeFETest.Entity.UserInfo;
import com.example.BeFETest.Entity.UserRequest;
import com.example.BeFETest.Entity.kosdak.KosdakResponse;
import com.example.BeFETest.Entity.kosdak2000.Kosdak2000Response;
import com.example.BeFETest.Entity.kospi.KospiResponse;
import com.example.BeFETest.Error.CustomExceptions;

import com.example.BeFETest.Repository.*;

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

    private final BacktestingHistoryRepository backtestingHistoryRepository;

    private final KospiRepository kospiRepository;

    private final Kosdak2000Repository kosdak2000Repository;

    private final KosdakRepository kosdakRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/mypage")
    public UserInfo checkUserInfo(@RequestBody UserRequest request) {
        UserEntity userEntity = userRepository.findByBirthDate(request.getUser_birth());
        if(userEntity == null) {
            throw new CustomExceptions.ResourceNotFoundException();
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


    @GetMapping("/home/kospi")
    public ResponseEntity<?> getKospi() {
        try{
            LocalDate currentDate = LocalDate.now();
            String currentDateString = currentDate.toString();
            List<KospiResponse> kospiResponses =
                kospiRepository.findByDate(currentDateString);
            if(!kospiResponses.isEmpty()) {
                return new ResponseEntity<>(kospiResponses, HttpStatus.OK);
            } else {
                throw new CustomExceptions.ResourceNotFoundException();
            }
        } catch (Exception e) {
            throw new CustomExceptions.InternalServerErrorException();
        }
    }

    @GetMapping("/home/kosdak")
    public ResponseEntity<?> getKosdak() {
        try {
            LocalDate currentDate = LocalDate.now();
            String currentDateString = currentDate.toString();
            List<KosdakResponse> kosdakResponses = 
                kosdakRepository.findByDate(currentDateString);
            if(!kosdakResponses.isEmpty()) {
                return new ResponseEntity<>(kosdakResponses, HttpStatus.OK);
            } else {
                throw new CustomExceptions.ResourceNotFoundException();
            }
        } catch (Exception e) {
            throw new CustomExceptions.InternalServerErrorException();
        }
    }
    
    @GetMapping("/home/kosdak2000")
    public ResponseEntity<?> getKosdak2000() {
        try {
            LocalDate currentDate = LocalDate.now();
            String currentDateString = currentDate.toString();
            List<Kosdak2000Response> kosdak2000Responses = 
                kosdak2000Repository.findByDate(currentDateString);
            if (!kosdak2000Responses.isEmpty()) {
                return new ResponseEntity<>(kosdak2000Responses, HttpStatus.OK);
            } else {
                throw new CustomExceptions.ResourceNotFoundException();
            }
        } catch (Exception e) {
            throw new CustomExceptions.InternalServerErrorException();
        }
    }    
        
        

    /*
    @PostMapping("/mypage")
    public UserInfo checkUserInfo(@RequestBody UserRequest request) {
        UserEntity userEntity = userRepository.findByBirthDate(request.getUser_birth());
        if (userEntity == null) {
            throw new RuntimeException("사용자 정보를 찾을 수 없습니다.");
        }
        System.out.println("Found User!");
        UserInfo userInfo = new UserInfo();
        userInfo.setName(userEntity.getName());
        userInfo.setPhone(userEntity.getPhoneNumber());
        userInfo.setBirthDate(userEntity.getBirthDate());
        userInfo.setGender(userEntity.getGender());
        userInfo.setBacktestingRecords(userEntity.getRecords().toArray(new String[0]));
        return userInfo;
    }
    */

    /*

    @PostMapping("/mypage")
    public UserInfo checkUserInfo(@RequestBody UserRequest request) {
        UserEntity userEntity = userRepository.findByBirthDate(request.getUser_birth());
        if (userEntity == null) {
            throw new RuntimeException("사용자 정보를 찾을 수 없습니다.");
        }
        System.out.println("Found User!");
        UserInfo userInfo = new UserInfo();
        userInfo.setName(userEntity.getName());
        userInfo.setPhone(userEntity.getPhoneNumber());
        userInfo.setBirthDate(userEntity.getBirthDate());
        userInfo.setGender(userEntity.getGender());

        // BacktestingHistory 객체를 문자열 배열로 변환
        String[] records = userEntity.getRecords().stream()
                .map(record -> "Date: " + record.getDate() +
                        ", Universe: " + record.getUniverse() +
                        ", Weight: " + record.getWeight() +
                        ", Initial Investment: " + record.getInitialInvestment() +
                        ", Period: " + record.getPeriod() +
                        ", File HTML: " + record.getFileHtml())
                .toArray(String[]::new);

        userInfo.setBacktestingRecords(records);

        System.out.println("Test-!!!");

        return userInfo;
    }
    */

    /*
    @GetMapping("/home/kospi")
    public ResponseEntity getKospi() {
        try {
            LocalDate currentDate = LocalDate.now();
            String currentDateString = currentDate.toString();
            List<KospiResponse> kospiResponses = kospiRepository.findByDate(currentDateString);
            if (!kospiResponses.isEmpty()) {
                return new ResponseEntity<>(kospiResponses, HttpStatus.OK);
            } else {
                //ErrorResponse errorResponse = new ErrorResponse(ErrorCode.RESOURCE_NOT_FOUND);
               // return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse(ErrorCode.INTERNAL_ERROR);
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/home/kosdak")
    public ResponseEntity getKosdak() {
        try {
            LocalDate currentDate = LocalDate.now();
            String currentDateString = currentDate.toString();
            List<KosdakResponse> kosdakResponses = kosdakRepository.findByDate(currentDateString);
            if (!kosdakResponses.isEmpty()) {
                return new ResponseEntity<>(kosdakResponses, HttpStatus.OK);
            } else {
                ErrorResponse errorResponse = new ErrorResponse(ErrorCode.RESOURCE_NOT_FOUND);
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse(ErrorCode.INTERNAL_ERROR);
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/home/kosdak2000")
    public ResponseEntity getKosdak2000() {
        try {
            LocalDate currentDate = LocalDate.now();
            String currentDateString = currentDate.toString();
            List<Kosdak2000Response> kosdak2000Responses = kosdak2000Repository.findByDate(currentDateString);
            if (!kosdak2000Responses.isEmpty()) {
                return new ResponseEntity<>(kosdak2000Responses, HttpStatus.OK);
            } else {
                ErrorResponse errorResponse = new ErrorResponse(ErrorCode.RESOURCE_NOT_FOUND);
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse(ErrorCode.INTERNAL_ERROR);
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    */

    /*
    @ExceptionHandler(InternalServerErrorException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleInternalServerErrorException(InternalServerErrorException ex){
        return ErrorResponse.of(ErrorCode.INTERNAL_ERROR);

    }
    */
}
