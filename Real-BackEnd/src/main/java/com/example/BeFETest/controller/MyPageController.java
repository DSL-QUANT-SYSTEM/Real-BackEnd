package com.example.BeFETest.controller;

import com.example.BeFETest.BusinessLogicLayer.myPage.MypageService;
import com.example.BeFETest.DTO.user.UserDTO;
import com.example.BeFETest.Entity.BacktestingRes.*;
import com.example.BeFETest.JWT.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import com.example.BeFETest.BusinessLogicLayer.BacktestingAuto.*;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MyPageController {

    @Autowired
    private JwtUtil jwtUtil;

    private final MypageService mypageService;
    private final BacktestingAutoGD backtestingAutoGD;
    private final BacktestingAutoBB backtestingAutoBB;
    private final BacktestingAutoInd backtestingAutoInd;
    private final BacktestingAutoEnv backtestingAutoEnv;
    private final BacktestingAutoWilliams backtestingAutoW;

    @GetMapping("/userinfo")
    public ResponseEntity<String> getUserName(@RequestHeader("Authorization") String token){
        try {
            UserDTO userInfo = mypageService.getUserInfo(token);
            return ResponseEntity.ok(userInfo.getName());
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(400).body("사용자 정보가 없습니다.");
        } catch (Exception e){
            return ResponseEntity.status(500).body("사용자 정보를 가져오는 도중 오류가 발생했습니다.");
        }
    }

    @GetMapping("/mypage/user")
    public ResponseEntity<?> getUserInfo(@RequestHeader("Authorization") String token){
        try {
            UserDTO userInfo = mypageService.getUserInfo(token);
            return ResponseEntity.ok(userInfo);
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(400).body("사용자 정보가 없습니다.");
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(500).body("사용자 정보를 가져오는 도중 오류가 발생했습니다.");
        }
    }

    @GetMapping("/user/info")
    public ResponseEntity<?> getUser(@RequestHeader("Authorization") String token){
        try {
            UserDTO userInfo = mypageService.getUserInfo(token);
            return ResponseEntity.ok(userInfo);
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(400).body("사용자 정보가 없습니다.");
        } catch (Exception e){
            return ResponseEntity.status(500).body("사용자 정보를 가져오는 도중 오류가 발생했습니다.");
        }
    }

    // 본인 DB에 백테스팅 더미데이터 넣기
    @GetMapping("/backtesting_mine_gd")
    public void getBacktestingResultsGD(@RequestHeader("Authorization") String token) {
        Long userId = jwtUtil.getUserIdFromToken(token);
        backtestingAutoGD.runAutomaticBacktesting(10,2, userId);
    }
    // 본인 DB에 백테스팅 더미데이터 넣기
    @GetMapping("/backtesting_mine_bb")
    public void getBacktestingResultsBB(@RequestHeader("Authorization") String token) {
        Long userId = jwtUtil.getUserIdFromToken(token);
        backtestingAutoBB.runAutomaticBacktesting(10,2,userId);
    }
    // 본인 DB에 백테스팅 더미데이터 넣기
    @GetMapping("/backtesting_mine_ind")
    public void getBacktestingResultsInd(@RequestHeader("Authorization") String token) {
        Long userId = jwtUtil.getUserIdFromToken(token);
        backtestingAutoInd.runAutomaticBacktesting(10,2,userId);
    }
    // 본인 DB에 백테스팅 더미데이터 넣기
    @GetMapping("/backtesting_mine_env")
    public void getBacktestingResultsEnv(@RequestHeader("Authorization") String token) {
        Long userId = jwtUtil.getUserIdFromToken(token);
        backtestingAutoEnv.runAutomaticBacktesting(10,2,userId);
    }
    // 본인 DB에 백테스팅 더미데이터 넣기
    @GetMapping("/backtesting_mine_williams")
    public void getBacktestingResultsW(@RequestHeader("Authorization") String token) {
        Long userId = jwtUtil.getUserIdFromToken(token);
        backtestingAutoW.runAutomaticBacktesting(10,2,userId);
    }


    @GetMapping("/history/golden")
    public ResponseEntity<?> getMyGolden(@RequestHeader("Authorization") String token){
        try {
            List<GDEntity> goldenData = mypageService.getAllGD(token);
            if (goldenData != null && !goldenData.isEmpty()) {
                return ResponseEntity.ok(goldenData);
            } else {
                return ResponseEntity.status(404).body("데이터를 찾을 수 없습니다.");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body("잘못된 요청입니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("서버 오류가 발생했습니다.");
        }
    }


    @GetMapping("/history/bollinger")
    public ResponseEntity<?> getMyBollinger(@RequestHeader("Authorization") String token){
        try {
            List<BBEntity> bollingerData = mypageService.getAllBB(token);
            if (bollingerData != null && !bollingerData.isEmpty()) {
                return ResponseEntity.ok(bollingerData);
            } else {
                return ResponseEntity.status(404).body("데이터를 찾을 수 없습니다.");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body("잘못된 요청입니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("서버 오류가 발생했습니다.");
        }
    }



    @GetMapping("/history/rsi")
    public ResponseEntity<?> getMyRsi(@RequestHeader("Authorization") String token){
        try {
            List<IndicatorEntity> indicatorData = mypageService.getAllIndi(token);
            if (indicatorData != null && !indicatorData.isEmpty()) {
                return ResponseEntity.ok(indicatorData);
            } else {
                return ResponseEntity.status(404).body("데이터를 찾을 수 없습니다.");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body("잘못된 요청입니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("서버 오류가 발생했습니다.");
        }
    }

    @GetMapping("/history/env")
    public ResponseEntity<?> getMyEnv(@RequestHeader("Authorization") String token){
        try {
            List<EnvEntity> envData = mypageService.getAllEnv(token);
            if (envData != null && !envData.isEmpty()) {
                return ResponseEntity.ok(envData);
            } else {
                return ResponseEntity.status(404).body("데이터를 찾을 수 없습니다.");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body("잘못된 요청입니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("서버 오류가 발생했습니다.");
        }
    }

    @GetMapping("/history/williams")
    public ResponseEntity<?> getMyW(@RequestHeader("Authorization") String token){
        try {
            List<WEntity> wData = mypageService.getAllW(token);
            if (wData != null && !wData.isEmpty()) {
                return ResponseEntity.ok(wData);
            } else {
                return ResponseEntity.status(404).body("데이터를 찾을 수 없습니다.");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body("잘못된 요청입니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("서버 오류가 발생했습니다.");
        }
    }

}
