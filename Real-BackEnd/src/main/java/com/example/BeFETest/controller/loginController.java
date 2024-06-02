package com.example.BeFETest.controller;

import com.example.BeFETest.BusinessLogicLayer.kakao.authService;
import com.example.BeFETest.DTO.kakaoDTO.LoginResponseDto;
import com.example.BeFETest.DTO.kakaoDTO.RefreshTokenRequest;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;

@RestController
public class loginController {

    private static final Logger logger = LoggerFactory.getLogger(loginController.class);

    @Autowired
    private authService authService;

    @GetMapping("/login/oauth2/code/kakao")
    public ResponseEntity<LoginResponseDto> login(@RequestParam("code") String code, HttpServletResponse response) {
        try {
            String kakaoAccessToken = authService.getKakaoAccessToken(code);
            ResponseEntity<LoginResponseDto> loginResponse = authService.kakaoLogin(kakaoAccessToken);

            if (loginResponse.getBody().isLoginSuccess()) {
                // 헤더에 JWT 토큰 설정
                String jwtToken = loginResponse.getHeaders().getFirst("Authorization");
                System.out.println("Generated JWT: " + jwtToken); // 디버깅용 출력
                response.setHeader("Authorization", jwtToken);
                return loginResponse;
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(loginResponse.getBody());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new LoginResponseDto());
        }

        /*
        try {
            String kakaoAccessToken = authService.getKakaoAccessToken(code);
            LoginResponseDto loginResponseDto = authService.kakaoLogin(kakaoAccessToken).getBody();

            if (loginResponseDto.isLoginSuccess()) {
                // 헤더에 JWT 토큰 설정
                response.setHeader("Authorization", "Bearer " + loginResponseDto.getJwtToken());
                return ResponseEntity.ok(loginResponseDto);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(loginResponseDto);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new LoginResponseDto());
        }

         */
        /*
        try {
            String kakaoAccessToken = authService.getKakaoAccessToken(code);
            return authService.kakaoLogin(kakaoAccessToken);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new LoginResponseDto());
        }

         */
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return authService.refreshToken(refreshTokenRequest.getRefreshToken());
    }

    @GetMapping("/api/user-info")
    public UserInfo getUserInfo(@RequestAttribute("claims") Claims claims) {
        logger.info("Received claims: " + claims);
        System.out.println("Received claims: " + claims);
        String username = claims.get("username", String.class);
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
