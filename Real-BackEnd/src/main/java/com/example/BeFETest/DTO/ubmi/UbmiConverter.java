package com.example.BeFETest.DTO.ubmi;



import com.example.BeFETest.Entity.ubmi.UbmiEntity;
import com.example.BeFETest.Entity.ubmi.UbmiResponse;

import java.util.List;
import java.util.stream.Collectors;

public class UbmiConverter {

    public static UbmiDTO toDto(UbmiEntity entity) {
        return new UbmiDTO(
                entity.getId(),
                entity.getTime(),
                entity.getValue()
        );
    }

    public static UbmiResponseDTO toDto(UbmiResponse response) {
        List<UbmiDTO> ubmiDataDto = response.getUbmiData().stream()
                .map(UbmiConverter::toDto)
                .collect(Collectors.toList());

        return new UbmiResponseDTO(
                response.getId(),
                response.getDate(),
                response.getCurrentPrice(),
                response.getAllDayRatio(),
                response.getPercentChange(),
                ubmiDataDto
        );
    }
}

