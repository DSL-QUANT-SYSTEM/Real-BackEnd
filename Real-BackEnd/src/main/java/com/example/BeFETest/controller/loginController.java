package com.example.BeFETest.controller;

import com.example.BeFETest.BusinessLogicLayer.kakao.authService;
import com.example.BeFETest.DTO.kakaoDTO.LoginResponseDto;
import com.example.BeFETest.DTO.kakaoDTO.RefreshTokenRequest;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new LoginResponseDto());
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return authService.refreshToken(refreshTokenRequest.getRefreshToken());
    }

    @GetMapping("/api/user-info")
    public UserInfo getUserInfo(@RequestAttribute("claims") Claims claims) {
        String username = claims.getSubject();
        String email = claims.get("email", String.class);
        return new UserInfo(username, email);
    }

    @Getter
    @Setter
    public static class UserInfo {
        private String username;
        private String email;

        public UserInfo(String username, String email) {
            this.username = username;
            this.email = email;
        }


        // Getters and Setters
    }

    /*
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

     */

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
