package com.example.BeFETest.DTO.kospi200;


import com.example.BeFETest.Entity.kospi200.Kospi200Response;


public class Kospi200Converter {

    /*
    public static Kospi200DTO toDto(Kospi200Entity entity) {
        return new Kospi200DTO(
                entity.getId(),
                entity.getTime(),
                entity.getValue()
        );
    }
    */

    public static Kospi200ResponseDTO toDto(Kospi200Response response) {

        /*
        List<Kospi200DTO> kospi200DataDto = response.getKospi200Data().stream()
                .map(Kospi200Converter::toDto)
                .collect(Collectors.toList());
                */


        return new Kospi200ResponseDTO(
                response.getId(),
                response.getDate(),
                response.getClosingPrice(),
                response.getComparison(),
                response.getFluctuationRate(),
                response.getOpeningPrice(),
                response.getHighPrice(),
                response.getLowPrice(),
                response.getTradingVolume(),
                response.getTradingAmount(),
                response.getListedCapitalization()
        );
    }
}

