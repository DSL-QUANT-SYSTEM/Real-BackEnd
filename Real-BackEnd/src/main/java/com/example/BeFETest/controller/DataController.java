package com.example.BeFETest.controller;

import com.example.BeFETest.BusinessLogicLayer.kosdak.KosdakResponseService;
import com.example.BeFETest.DTO.kosdak.KosdakResponseDTO;
import com.example.BeFETest.DTO.user.UserDTO;
import com.example.BeFETest.JWT.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/kosdak")
@RequiredArgsConstructor
public class DataController {

    ///api/kosdak/last-year

    private final KosdakResponseService kosdakService;
    private final JwtUtil jwtUtil;

    @GetMapping("/last-year")
    public List<KosdakResponseDTO> getKosdakYearData(){
        return kosdakService.getResponsesByYear();
    }

    @GetMapping("/mypage")
    public ResponseEntity<UserDTO> getMyPage(@RequestHeader("Authorization") String token){
        Long userId = jwtUtil.getUserIdFromToken(token);
        return null;
    }

}
