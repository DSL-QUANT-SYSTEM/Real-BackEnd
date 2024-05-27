package com.example.BeFETest.DTO.kakaoDTO;

import lombok.Data;

@Data
public class LoginResponseDto {
    private boolean loginSuccess;
    private Account account;
    private String jwtToken;

}
