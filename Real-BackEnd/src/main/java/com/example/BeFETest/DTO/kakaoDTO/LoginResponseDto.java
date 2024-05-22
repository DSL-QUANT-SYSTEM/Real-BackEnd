package com.example.BeFETest.DTO.kakaoDTO;

import lombok.Data;

@Data
public class LoginResponseDto {
    public boolean loginSuccess;
    public Account account;
}
