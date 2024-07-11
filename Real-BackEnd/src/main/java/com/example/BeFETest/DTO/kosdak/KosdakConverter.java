package com.example.BeFETest.DTO.kosdak;

import com.example.BeFETest.Entity.kosdak.KosdakEntity;
import com.example.BeFETest.Entity.kosdak.KosdakResponse;

import java.util.List;
import java.util.stream.Collectors;

public class KosdakConverter {

    public static KosdakDTO toDto(KosdakEntity entity) {
        return new KosdakDTO(
                entity.getId(),
                entity.getTime(),
                entity.getValue()
        );
    }

    public static KosdakResponseDTO toDto(KosdakResponse response) {
        List<KosdakDTO> kosdakDataDto = response.getKosdakData().stream()
                .map(KosdakConverter::toDto)
                .collect(Collectors.toList());

        return new KosdakResponseDTO(
                response.getId(),
                response.getDate(),
                response.getCurrentPrice(),
                response.getAllDayRatio(),
                response.getPercentChange(),
                kosdakDataDto
        );
    }
}

