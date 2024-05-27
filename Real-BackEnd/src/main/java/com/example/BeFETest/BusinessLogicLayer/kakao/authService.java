package com.example.BeFETest.BusinessLogicLayer.kakao;

import com.example.BeFETest.JWT.JwtUtil;
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

import com.example.BeFETest.DTO.kakaoDTO.Account;
import com.example.BeFETest.DTO.kakaoDTO.KakaoAccountDto;
import com.example.BeFETest.DTO.kakaoDTO.LoginResponseDto;
import com.example.BeFETest.DTO.kakaoDTO.KakaoTokenDto;

import com.example.BeFETest.Repository.kakao.accountRepo;
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

    /*
    public ResponseEntity<LoginResponseDto> kakaoLogin(String kakaoAccessToken) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        Account account = getKakaoInfo(kakaoAccessToken);

        LoginResponseDto loginResponseDto = new LoginResponseDto();
        loginResponseDto.setLoginSuccess(true);
        loginResponseDto.setAccount(account);

        Account existOwner = accountRepo.findById(account.getId()).orElse(null);
        try {
            if (existOwner == null) {
                accountRepo.save(account);
            }
            String jwt = jwtUtil.generateToken(account.getId());
            headers.add("Authorization", "Bearer " + jwt);

            loginResponseDto.setLoginSuccess(true);
            return ResponseEntity.ok().headers(headers).body(loginResponseDto);

        } catch (Exception e) {
            e.printStackTrace();
            loginResponseDto.setLoginSuccess(false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(loginResponseDto);
        }
    }

     */

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

            String jwt = jwtUtil.generateToken(account.getId());  // or account.getId() if you prefer
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
    public Account getKakaoInfo(String kakaoAccessToken) throws JsonProcessingException {
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
        KakaoAccountDto kakaoAccountDto = objectMapper.readValue(accountInfoResponse.getBody(), KakaoAccountDto.class);

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
    public String getKakaoAccessToken(String code) {
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

        KakaoTokenDto kakaoTokenDto = null;
        try {
            kakaoTokenDto = objectMapper.readValue(accessTokenResponse.getBody(), KakaoTokenDto.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return kakaoTokenDto.getAccess_token();
    }

    public ResponseEntity<LoginResponseDto> kakaoLogin(String kakaoAccessToken) {
        HttpHeaders headers = new HttpHeaders();
        Account account = getKakaoInfo(kakaoAccessToken);

        LoginResponseDto loginResponseDto = new LoginResponseDto();
        loginResponseDto.setLoginSuccess(true);
        loginResponseDto.setAccount(account);

        Account existOwner = accountRepo.findById(account.getId()).orElse(null);
        try {
            if (existOwner == null) {
                accountRepo.save(account);
            }
            String jwt = jwtUtil.generateToken(account.getId());
            headers.add("Authorization", "Bearer " + jwt);

            loginResponseDto.setLoginSuccess(true);
            return ResponseEntity.ok().headers(headers).body(loginResponseDto);

        } catch (Exception e) {
            loginResponseDto.setLoginSuccess(false);
            return ResponseEntity.status(500).body(loginResponseDto);
        }
    }

    public Account getKakaoInfo(String kakaoAccessToken) {
        HttpHeaders headers = new HttpHeaders();
        RestTemplate rt = new RestTemplate();
        headers.add("Authorization", "Bearer " + kakaoAccessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> accountInfoRequest = new HttpEntity<>(headers);

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

        Long kakaoId = kakaoAccountDto.getId();
        String username = kakaoAccountDto.getProperties().getNickname();
        Account existOwner = accountRepo.findById(kakaoId).orElse(null);

        return Account.builder()
                .id(kakaoId)
                .email(kakaoAccountDto.getKakao_account().getEmail())
                .username(username)
                .build();
    }
}

 */

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


    @Transactional
    public String getKakaoAccessToken(String code){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        //Http Response Body 객체 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", CLIENT_ID);
        params.add("redirect_uri", REDIRECT_URI);
        params.add("code", code);

        HttpEntity<MultiValueMap<String,String>> kakaoTokenRequest = new HttpEntity<>(params,headers);

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

        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);

        KakaoTokenDto kakaoTokenDto = null;
        try{
            kakaoTokenDto = objectMapper.readValue(accessTokenResponse.getBody(), KakaoTokenDto.class);
        }
        catch(JsonProcessingException e){
            e.printStackTrace();
        }
        return kakaoTokenDto.getAccess_token();
    }
    public ResponseEntity<LoginResponseDto> kakaoLogin(String kakaoAccessToken) {
        HttpHeaders headers = new HttpHeaders();
        Account account = getKakaoInfo(kakaoAccessToken);

        LoginResponseDto loginResponseDto = new LoginResponseDto();
        loginResponseDto.setLoginSuccess(true);
        loginResponseDto.setAccount(account);

        Account existOwner = accountRepo.findById(account.getId()).orElse(null);
        try {
            if (existOwner == null) {//처음 로그인
                //System.out.println("first time login user");
                accountRepo.save(account);
            }
            loginResponseDto.setLoginSuccess(true);

            return ResponseEntity.ok().headers(headers).body(loginResponseDto);

        } catch (Exception e) {
            loginResponseDto.setLoginSuccess(false);
            return ResponseEntity.badRequest().body(loginResponseDto);
        }
    }

    public Account getKakaoInfo(String kakaoAccessToken) {
        HttpHeaders headers = new HttpHeaders();
        RestTemplate rt = new RestTemplate();
        headers.add("Authorization", "Bearer " + kakaoAccessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> accountInfoRequest = new HttpEntity<>(headers);

        // POST 방식으로 API 서버에 요청 후 response 받아옴
        ResponseEntity<String> accountInfoResponse = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                accountInfoRequest,
                String.class
        );
        System.out.println("받아올 수 있는 것들: " + accountInfoResponse.getBody());

        // JSON Parsing (-> kakaoAccountDto)
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        KakaoAccountDto kakaoAccountDto = null;
        try {
            kakaoAccountDto = objectMapper.readValue(accountInfoResponse.getBody(), KakaoAccountDto.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        // 회원가입 처리하기
        Long kakaoId = kakaoAccountDto.getId();

        String username = kakaoAccountDto.getProperties().getNickname();
        //System.out.println("kakao ID: " + kakaoId); //userID확인용
        //System.out.println("kakao UserName" + username);//user name 확인용
        Account existOwner = accountRepo.findById(kakaoId).orElse(null);

        return Account.builder()
                .id(kakaoAccountDto.getId())
                .email(kakaoAccountDto.getKakao_account().getEmail())
                .username(kakaoAccountDto.getProperties().getNickname())
                .build();
    }
}

 */