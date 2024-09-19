package com.example.BeFETest.DTO.SchedulingCoin;

import com.example.BeFETest.Entity.SchedulingCoin.SchedulingCoinResponse;

public class SchedulingCoinConverter {

    public static SchedulingCoinDTO toDto(SchedulingCoinResponse coinResponse){

        return new SchedulingCoinDTO(
                coinResponse.getDate(),
                coinResponse.getClosingPrice(),
                coinResponse.getFluctuatingRate(),
                coinResponse.getOpeningPrice(),
                coinResponse.getHighPrice(),
                coinResponse.getLowPrice(),
                coinResponse.getMarket(),
                coinResponse.getTradingVolume()
        );
    }
}
