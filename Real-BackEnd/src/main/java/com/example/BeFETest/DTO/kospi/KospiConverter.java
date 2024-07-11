package com.example.BeFETest.DTO.kospi;

import com.example.BeFETest.DTO.kosdak.KosdakDTO;
import com.example.BeFETest.DTO.kosdak.KosdakResponseDTO;
import com.example.BeFETest.Entity.kosdak.KosdakEntity;
import com.example.BeFETest.Entity.kosdak.KosdakResponse;
import com.example.BeFETest.Entity.kospi.KospiEntity;
import com.example.BeFETest.Entity.kospi.KospiResponse;

import java.util.List;
import java.util.stream.Collectors;

public class KospiConverter {

    public static KospiDTO toDto(KospiEntity entity) {
        return new KospiDTO(
                entity.getId(),
                entity.getTime(),
                entity.getValue()
        );
    }

    public static KospiResponseDTO toDto(KospiResponse response) {
        List<KospiDTO> kospiDataDto = response.getKospiData().stream()
                .map(KospiConverter::toDto)
                .collect(Collectors.toList());

        return new KospiResponseDTO(
                response.getId(),
                response.getDate(),
                response.getCurrentPrice(),
                response.getAllDayRatio(),
                response.getPercentChange(),
                kospiDataDto
        );
    }
}

