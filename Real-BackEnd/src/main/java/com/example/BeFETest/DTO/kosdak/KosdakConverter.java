package com.example.BeFETest.DTO.kosdak;

import com.example.BeFETest.Entity.kosdak.KosdakResponse;

import java.util.List;
import java.util.stream.Collectors;

public class KosdakConverter {


    public static KosdakResponseDTO toDto(KosdakResponse response) {

        return new KosdakResponseDTO(
                response.getDate(),
                response.getClosingPrice(),
                response.getFluctuatingRate(),
                response.getOpeningPrice(),
                response.getHighPrice(),
                response.getLowPrice(),
                response.getTradingVolume()
        );
    }
}

