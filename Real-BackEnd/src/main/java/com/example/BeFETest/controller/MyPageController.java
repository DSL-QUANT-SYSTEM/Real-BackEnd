package com.example.BeFETest.controller;

import com.example.BeFETest.BusinessLogicLayer.Strategy.StrategyService;
import com.example.BeFETest.BusinessLogicLayer.myPage.MypageService;
import com.example.BeFETest.DTO.user.UserDTO;
import com.example.BeFETest.Entity.BacktestingRes.BBEntity;
import com.example.BeFETest.Entity.BacktestingRes.GDEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MyPageController {

    private final MypageService mypageService;

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

    @GetMapping("/history/golden")
    public ResponseEntity<?> getMyGolden(@RequestHeader("Authorization") String token){
        try {
            List<GDEntity> goldenData = mypageService.getTop10GD(token);
            if (goldenData != null && !goldenData.isEmpty()) {
                return ResponseEntity.ok(goldenData);
            } else {
                return ResponseEntity.status(404).body("데이터를 찾을 수 없습니다.");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body("잘못된 요청입니다.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("서버 오류가 발생했습니다.");
        }
    }

    @GetMapping("/history/bollinger")
    public ResponseEntity<?> getMyBollinger(@RequestHeader("Authorization") String token){
        try {
            List<BBEntity> bollingerData = mypageService.getTop10BB(token);
            if (bollingerData != null && !bollingerData.isEmpty()) {
                return ResponseEntity.ok(bollingerData);
            } else {
                return ResponseEntity.status(404).body("데이터를 찾을 수 없습니다.");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body("잘못된 요청입니다.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("서버 오류가 발생했습니다.");
        }
    }


}
