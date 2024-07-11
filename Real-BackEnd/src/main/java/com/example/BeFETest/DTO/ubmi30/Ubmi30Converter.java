package com.example.BeFETest.DTO.ubmi30;


import com.example.BeFETest.Entity.ubmi30.Ubmi30Entity;
import com.example.BeFETest.Entity.ubmi30.Ubmi30Response;

import java.util.List;
import java.util.stream.Collectors;

public class Ubmi30Converter {

    public static Ubmi30DTO toDto(Ubmi30Entity entity) {
        return new Ubmi30DTO(
                entity.getId(),
                entity.getTime(),
                entity.getValue()
        );
    }

    public static Ubmi30ResponseDTO toDto(Ubmi30Response response) {
        List<Ubmi30DTO> ubmi30DataDto = response.getUbmi30Data().stream()
                .map(Ubmi30Converter::toDto)
                .collect(Collectors.toList());

        return new Ubmi30ResponseDTO(
                response.getId(),
                response.getDate(),
                response.getCurrentPrice(),
                response.getAllDayRatio(),
                response.getPercentChange(),
                ubmi30DataDto
        );
    }
}

