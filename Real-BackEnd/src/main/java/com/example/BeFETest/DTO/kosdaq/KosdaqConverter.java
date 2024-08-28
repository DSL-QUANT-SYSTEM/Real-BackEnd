package com.example.BeFETest.DTO.kosdaq;

import com.example.BeFETest.Entity.kosdaq.KosdaqResponse;

public class KosdaqConverter {


    public static KosdaqResponseDTO toDto(KosdaqResponse response) {

        return new KosdaqResponseDTO(
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

