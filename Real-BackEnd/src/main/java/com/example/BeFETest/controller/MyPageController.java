package com.example.BeFETest.controller;

import com.example.BeFETest.BusinessLogicLayer.myPage.MypageService;
import com.example.BeFETest.DTO.user.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MyPageController {

    private final MypageService service;

    @GetMapping("/userinfo")
    public ResponseEntity<?> getUserInfo(@RequestHeader("Authorization") String token){
        try {
            UserDTO userInfo = service.getUserInfo(token);
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
            UserDTO userInfo = service.getUserInfo(token);
            return ResponseEntity.ok(userInfo);
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(400).body("사용자 정보가 없습니다.");
        } catch (Exception e){
            return ResponseEntity.status(500).body("사용자 정보를 가져오는 도중 오류가 발생했습니다.");
        }
    }
}
