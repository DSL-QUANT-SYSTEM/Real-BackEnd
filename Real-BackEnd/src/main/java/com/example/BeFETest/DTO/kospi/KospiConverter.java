package com.example.BeFETest.DTO.kospi;

import com.example.BeFETest.Entity.kospi.KospiResponse;


public class KospiConverter {

    /*
    public static KospiDTO toDto(KospiEntity entity) {
        return new KospiDTO(
                entity.getId(),
                entity.getTime(),
                entity.getValue()
        );
    }
    */

    public static KospiResponseDTO toDto(KospiResponse response) {

        return new KospiResponseDTO(
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

