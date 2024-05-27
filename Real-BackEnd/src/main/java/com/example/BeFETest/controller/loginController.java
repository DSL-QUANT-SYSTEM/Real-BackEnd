package com.example.BeFETest.controller;

import com.example.BeFETest.BusinessLogicLayer.kakao.authService;
import com.example.BeFETest.DTO.kakaoDTO.LoginResponseDto;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;

@RestController
public class loginController {
    @Autowired
    private authService authService;

    @GetMapping("/login/oauth2/code/kakao")
    public ResponseEntity<LoginResponseDto> login(@RequestParam("code") String code, HttpServletResponse response) {
        try {
            String kakaoAccessToken = authService.getKakaoAccessToken(code);
            return authService.kakaoLogin(kakaoAccessToken);
        } catch (Exception e) {
            // 에러 처리 및 로그 기록
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new LoginResponseDto());
        }
    }

    /*
    @Autowired
    private authService authService = new authService();

    @GetMapping("/login/oauth2/code/kakao")
    public ResponseEntity<LoginResponseDto> login(@RequestParam("code") String code, HttpServletResponse response) throws URISyntaxException {
        //System.out.println("code :" + code); //인가 코드 확인용
        String kakaoAccessToken = authService.getKakaoAccessToken(code);
        //System.out.println("Token :" + kakaoAccessToken);//token 확인용
        return authService.kakaoLogin(kakaoAccessToken);
    }

     */
}
