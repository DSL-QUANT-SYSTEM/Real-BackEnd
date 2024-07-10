package com.example.BeFETest.dto.kakaoDTO;

import lombok.Data;

@Data
public class LoginResponseDto {

    private boolean loginSuccess;
    private Account account;
    private String jwtToken;
    private String refreshToken;

}
