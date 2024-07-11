package com.example.BeFETest.DTO.ubai;


import com.example.BeFETest.Entity.ubai.UbaiEntity;
import com.example.BeFETest.Entity.ubai.UbaiResponse;

import java.util.List;
import java.util.stream.Collectors;

public class UbaiConverter {

    public static UbaiDTO toDto(UbaiEntity entity) {
        return new UbaiDTO(
                entity.getId(),
                entity.getTime(),
                entity.getValue()
        );
    }

    public static UbaiResponseDTO toDto(UbaiResponse response) {
        List<UbaiDTO> ubaiDataDto = response.getUbaiData().stream()
                .map(UbaiConverter::toDto)
                .collect(Collectors.toList());

        return new UbaiResponseDTO(
                response.getId(),
                response.getDate(),
                response.getCurrentPrice(),
                response.getAllDayRatio(),
                response.getPercentChange(),
                ubaiDataDto
        );
    }
}

