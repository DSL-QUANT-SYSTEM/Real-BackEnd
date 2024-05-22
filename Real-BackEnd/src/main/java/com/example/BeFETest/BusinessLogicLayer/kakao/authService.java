package com.example.BeFETest.BusinessLogicLayer.kakao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.example.BeFETest.DTO.kakaoDTO.Account;
import com.example.BeFETest.DTO.kakaoDTO.KakaoAccountDto;
import com.example.BeFETest.DTO.kakaoDTO.LoginResponseDto;
import com.example.BeFETest.DTO.kakaoDTO.KakaoTokenDto;

import com.example.BeFETest.Repository.accountRepo;

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
        System.out.println("hello from kakaoLogin function: " + account.getId() +"    " + account.getEmail());

        LoginResponseDto loginResponseDto = new LoginResponseDto();
        loginResponseDto.setLoginSuccess(true);
        loginResponseDto.setAccount(account);

        Account existOwner = accountRepo.findById(account.getId()).orElse(null);
        try {
            if (existOwner == null) {
                System.out.println("first time login user");
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
        System.out.println("내용" + accountInfoResponse.getBody());

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
        System.out.println("kakao ID: " + kakaoId);
        System.out.println("kakao UserName" + username);
        Account existOwner = accountRepo.findById(kakaoId).orElse(null);

        // 처음 로그인이 아닌 경우
//        if (existOwner != null) {
//            return Account.builder()
//                    .id(kakaoAccountDto.getId())
//                    .email(kakaoAccountDto.getKakao_account().getEmail())
//                    .username(kakaoAccountDto.getProperties().getNickname())
//                    .build();
//        }
//        // 처음 로그인 하는 경우(kakaoId = null)
//        else {
//            return Account.builder()
//                    .id(kakaoAccountDto.getId())
//                    .email(kakaoAccountDto.getKakao_account().getEmail())
//                    .username(kakaoAccountDto.getProperties().getNickname())
//                    .build();
//        }
        return Account.builder()
                .id(kakaoAccountDto.getId())
                .email(kakaoAccountDto.getKakao_account().getEmail())
                .username(kakaoAccountDto.getProperties().getNickname())
                .build();
    }
}