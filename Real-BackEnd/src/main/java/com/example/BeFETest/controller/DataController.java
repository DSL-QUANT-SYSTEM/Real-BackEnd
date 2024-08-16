package com.example.BeFETest.controller;

import com.example.BeFETest.BusinessLogicLayer.kosdak.KosdakResponseService;
import com.example.BeFETest.DTO.kosdak.KosdakResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/kosdak")
@RequiredArgsConstructor
public class DataController {

    ///api/kosdak/last-year

    private final KosdakResponseService kosdakService;

    @GetMapping("/last-year")
    public List<KosdakResponseDTO> getKosdakYearData(){
        return kosdakService.getResponsesByYear();
    }

}
