package com.example.BeFETest.controller;

import com.example.BeFETest.BusinessLogicLayer.kakao.authService;
import com.example.BeFETest.DTO.kakaoDTO.LoginResponseDto;
import com.example.BeFETest.DTO.kakaoDTO.RefreshTokenRequest;
import com.example.BeFETest.Entity.RefreshToken;
import com.example.BeFETest.Error.CustomExceptions;
import com.example.BeFETest.Error.ErrorCode;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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

            if(loginResponse.getBody().isLoginSuccess()) {
                //헤더에 JWT 토큰 설정
                String jwtToken = loginResponse.getHeaders().getFirst("Authorization");
                response.setHeader("Authorization", jwtToken);
                return loginResponse;
            } else {
                throw new CustomExceptions.UnauthorizedException("Unathorized Error", null, "Unathorized Error", ErrorCode.UNAUTHORIZED);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomExceptions.InternalServerErrorException("Error message : " + e.getMessage(), e, "Error message : " + e.getMessage(), ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
        
            

    
    /*
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
    }
    */

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        try {
            return authService.refreshToken(refreshTokenRequest.getRefreshToken());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomExceptions.InternalServerErrorException("Error message : " + e.getMessage(), e, "Error message : " + e.getMessage(), ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
        

    /*
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return authService.refreshToken(refreshTokenRequest.getRefreshToken());
    }
    */

    @GetMapping("/api/user-info")
    public UserInfo getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.getPrincipal() instanceof String) {
            String username = (String) authentication.getPrincipal();
            Claims claims = (Claims) authentication.getDetails();
            String email = claims.get("email", String.class);
            return new UserInfo(username, email);
        } else {
            throw new CustomExceptions.ForbiddenException("Forbidden Error", null, "Forbidden Error", ErrorCode.FORBIDDEN);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) {
        try {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            Long userId = authService.getJwtUtil().getUserIdFromToken(token);
            authService.logout(userId);
            return ResponseEntity.ok().body("Successfully logged out");
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomExceptions.InternalServerErrorException("Error message : " + e.getMessage(), e, "Error message : " + e.getMessage(), ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }


    /*
    @GetMapping("/api/user-info")
    public UserInfo getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof String) {
            String username = (String) authentication.getPrincipal();
            Claims claims = (Claims) authentication.getDetails();

            //logger.info("Received claims: " + claims);
            //System.out.println("Received claims: " + claims);
            
            String email = claims.get("email", String.class);
            return new UserInfo(username, email);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
    }
    */

    @Getter
    @Setter
    public static class UserInfo {
        private String username;
        private String email;

        public UserInfo(String username, String email) {
            this.username = username;
            this.email = email;
        }

    }

   
}
