package com.example.BeFETest.DTO.kakaoDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtResponse {
    private String jwtToken;
    private String refreshToken;

    public JwtResponse(String jwtToken, String refreshToken) {
        this.jwtToken = jwtToken;
        this.refreshToken = refreshToken;
    }

    // Getters and Setters
}

