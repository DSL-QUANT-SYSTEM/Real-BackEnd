package com.example.BeFETest.controller;

import com.example.BeFETest.BusinessLogicLayer.kakao.authService;
import com.example.BeFETest.DTO.kakaoDTO.Account;
import com.example.BeFETest.DTO.kakaoDTO.LoginResponseDto;
import com.example.BeFETest.DTO.kakaoDTO.RefreshTokenRequest;
import com.example.BeFETest.DTO.user.UserDTO;
import com.example.BeFETest.Entity.RefreshToken;
import com.example.BeFETest.Error.CustomExceptions;
import com.example.BeFETest.Error.ErrorCode;
import com.example.BeFETest.Repository.kakao.accountRepo;
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
import java.util.Objects;
import java.util.Optional;

@RestController
public class loginController {


    private static final Logger logger = LoggerFactory.getLogger(loginController.class);

    @Autowired
    private authService authService;

    @Autowired
    private accountRepo accountRepository;


    @GetMapping("/login/oauth2/code/kakao")
    public ResponseEntity<LoginResponseDto> login(@RequestParam("code") String code, HttpServletResponse response) {
        try {
            String kakaoAccessToken = authService.getKakaoAccessToken(code);
            System.out.println(kakaoAccessToken);
            ResponseEntity<LoginResponseDto> loginResponse = authService.kakaoLogin(kakaoAccessToken);

            if (Objects.requireNonNull(loginResponse.getBody()).isLoginSuccess()) {
                // 헤더에 JWT 토큰 설정
                String jwtToken = loginResponse.getHeaders().getFirst("Authorization");
                response.setHeader("Authorization", jwtToken);
                return loginResponse;
            } else {
                throw new CustomExceptions.UnauthorizedException("Unauthorized Error", null, "Unauthorized Error", ErrorCode.UNAUTHORIZED);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 원래 에러 메시지를 그대로 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new LoginResponseDto());
        }
    }



    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        try {
            return authService.refreshToken(refreshTokenRequest.getRefreshToken());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomExceptions.InternalServerErrorException("Error message : " + e.getMessage(), e, "Error message : " + e.getMessage(), ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }



    @GetMapping("/api/user-info")
    public ResponseEntity<?> getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        String username = authentication.getName();  // SecurityContext에서 사용자 이름을 가져옴

        // 사용자 정보를 조회하고 반환
        Optional<Account> accountOptional = accountRepository.findByUsername(username);

        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();
            UserDTO userDTO = new UserDTO(account.getUsername(), account.getEmail());
            return ResponseEntity.ok(userDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
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
