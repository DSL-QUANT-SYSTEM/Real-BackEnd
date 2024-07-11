package com.example.BeFETest.DTO.kosdak2000;

import com.example.BeFETest.Entity.kosdak2000.Kosdak2000Entity;
import com.example.BeFETest.Entity.kosdak2000.Kosdak2000Response;

import java.util.List;
import java.util.stream.Collectors;

public class Kosdak2000Converter {

    public static Kosdak2000DTO toDto(Kosdak2000Entity entity) {
        return new Kosdak2000DTO(
                entity.getId(),
                entity.getTime(),
                entity.getValue()
        );
    }

    public static Kosdak2000ResponseDTO toDto(Kosdak2000Response response) {
        List<Kosdak2000DTO> kosdak2000DataDto = response.getKosdak2000Data().stream()
                .map(Kosdak2000Converter::toDto)
                .collect(Collectors.toList());

        return new Kosdak2000ResponseDTO(
                response.getId(),
                response.getDate(),
                response.getCurrentPrice(),
                response.getAllDayRatio(),
                response.getPercentChange(),
                kosdak2000DataDto
        );
    }
}

