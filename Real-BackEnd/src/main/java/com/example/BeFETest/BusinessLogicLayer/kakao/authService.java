package com.example.BeFETest.BusinessLogicLayer.kakao;

import com.example.BeFETest.DTO.kakaoDTO.*;
import com.example.BeFETest.Entity.RefreshToken;
import com.example.BeFETest.JWT.JwtUtil;
import com.example.BeFETest.Repository.RefreshTokenRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.example.BeFETest.Repository.kakao.accountRepo;

import java.util.Date;

@Service
public class authService {

    @Value("${kakao.client-id}")
    private String CLIENT_ID;
    @Value("${kakao.redirect-uri}")
    private String REDIRECT_URI;
    @Value("${kakao.token-uri}")
    private String KAKAO_TOKEN_URI;

    @Autowired
    private accountRepo accountRepo;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public String getKakaoAccessToken(String code) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", CLIENT_ID);
        params.add("redirect_uri", REDIRECT_URI);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> accessTokenResponse = rt.exchange(
                KAKAO_TOKEN_URI,
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        KakaoTokenDto kakaoTokenDto = objectMapper.readValue(accessTokenResponse.getBody(), KakaoTokenDto.class);

        return kakaoTokenDto.getAccess_token();
    }

    public ResponseEntity<LoginResponseDto> kakaoLogin(String kakaoAccessToken) {
        HttpHeaders headers = new HttpHeaders();
        LoginResponseDto loginResponseDto = new LoginResponseDto();
        Account account;

        try {
            account = getKakaoInfo(kakaoAccessToken);
            if (account == null) {
                throw new IllegalArgumentException("Failed to fetch account information from Kakao");
            }
        } catch (Exception e) {
            loginResponseDto.setLoginSuccess(false);
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(loginResponseDto);
        }

        try {
            Account existOwner = accountRepo.findById(account.getId()).orElse(null);
            if (existOwner == null) {
                accountRepo.save(account);
            }

            String jwt = jwtUtil.generateToken(account.getId());
            String refreshJwt = jwtUtil.generateRefreshToken(account.getId());
            saveRefreshToken(account.getId(), refreshJwt);
            headers.add("Authorization", "Bearer " + jwt);

            loginResponseDto.setLoginSuccess(true);
            loginResponseDto.setAccount(account);
            loginResponseDto.setJwtToken(jwt);
            loginResponseDto.setRefreshToken(refreshJwt);
            return ResponseEntity.ok().headers(headers).body(loginResponseDto);
        } catch (Exception e) {
            loginResponseDto.setLoginSuccess(false);
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(loginResponseDto);
        }
    }

    private void saveRefreshToken(Long userId, String refreshToken) {
        RefreshToken token = new RefreshToken();
        token.setUserId(userId);
        token.setToken(refreshToken);
        token.setExpiryDate(new Date(System.currentTimeMillis() + jwtUtil.getRefreshExpirationInMs()));
        refreshTokenRepository.save(token);
    }

    public ResponseEntity<?> refreshToken(String refreshToken) {
        if (jwtUtil.validateToken(refreshToken) && !jwtUtil.isTokenExpired(refreshToken)) {
            Long userId = jwtUtil.getUserIdFromToken(refreshToken);
            String newJwt = jwtUtil.generateToken(userId);
            return ResponseEntity.ok(new JwtResponse(newJwt, refreshToken));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired refresh token");
        }
    }

    public Account getKakaoInfo(String kakaoAccessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + kakaoAccessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> accountInfoRequest = new HttpEntity<>(headers);

        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> accountInfoResponse = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                accountInfoRequest,
                String.class
        );

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        KakaoAccountDto kakaoAccountDto = null;
        try {
            kakaoAccountDto = objectMapper.readValue(accountInfoResponse.getBody(), KakaoAccountDto.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return Account.builder()
                .id(kakaoAccountDto.getId())
                .email(kakaoAccountDto.getKakao_account().getEmail())
                .username(kakaoAccountDto.getProperties().getNickname())
                .build();
    }
}



/*
@Service
public class authService {

    @Value("${kakao.client-id}")
    private String CLIENT_ID;
    @Value("${kakao.redirect-uri}")
    private String REDIRECT_URI;
    @Value("${kakao.token-uri}")
    private String KAKAO_TOKEN_URI;

    @Autowired
    private accountRepo accountRepo;

    @Autowired
    private JwtUtil jwtUtil;

    @Transactional
    public String getKakaoAccessToken(String code) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        //Http Response Body 객체 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", CLIENT_ID);
        params.add("redirect_uri", REDIRECT_URI);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> accessTokenResponse = rt.exchange(
                KAKAO_TOKEN_URI,
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        //Json Parsing
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        KakaoTokenDto kakaoTokenDto = objectMapper.readValue(accessTokenResponse.getBody(), KakaoTokenDto.class);

        //밑의 방식으로 예외 처리 해줄거면 throws Exception 없애야됨
        //KakaoTokenDto kakaoTokenDto = null;
        //try{
        //    kakaoTokenDto = objectMapper.readValue(accessTokenResponse.getBody(), KakaoTokenDto.class);
        //}
        //catch(JsonProcessingException e){
        //    e.printStackTrace();
        //}
        
        return kakaoTokenDto.getAccess_token();
    }
    
    public ResponseEntity<LoginResponseDto> kakaoLogin(String kakaoAccessToken) {
        HttpHeaders headers = new HttpHeaders();
        LoginResponseDto loginResponseDto = new LoginResponseDto();
        Account account;

        try {
            account = getKakaoInfo(kakaoAccessToken);
            if (account == null) {
                throw new IllegalArgumentException("Failed to fetch account information from Kakao");
            }
        } catch (Exception e) {
            loginResponseDto.setLoginSuccess(false);
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(loginResponseDto);
        }

        try {
            Account existOwner = accountRepo.findById(account.getId()).orElse(null);
            if (existOwner == null) {
                accountRepo.save(account);
            }

            String jwt = jwtUtil.generateToken(account.getId());  
            headers.add("Authorization", "Bearer " + jwt);

            loginResponseDto.setLoginSuccess(true);
            loginResponseDto.setAccount(account);
            loginResponseDto.setJwtToken(jwt);  // Set the JWT token in the response DTO
            return ResponseEntity.ok().headers(headers).body(loginResponseDto);
        } catch (Exception e) {
            loginResponseDto.setLoginSuccess(false);
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(loginResponseDto);
        }
    }
    
    public Account getKakaoInfo(String kakaoAccessToken) { //throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + kakaoAccessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> accountInfoRequest = new HttpEntity<>(headers);

        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> accountInfoResponse = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                accountInfoRequest,
                String.class
        );

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        
        //throws로 예외 처리 안하는 경우
        //KakaoAccountDto kakaoAccountDto = objectMapper.readValue(accountInfoResponse.getBody(), KakaoAccountDto.class);

        KakaoAccountDto kakaoAccountDto = null;
        try {
            kakaoAccountDto = objectMapper.readValue(accountInfoResponse.getBody(), KakaoAccountDto.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


        return Account.builder()
                .id(kakaoAccountDto.getId())
                .email(kakaoAccountDto.getKakao_account().getEmail())
                .username(kakaoAccountDto.getProperties().getNickname())
                .build();
    }
}

 */

