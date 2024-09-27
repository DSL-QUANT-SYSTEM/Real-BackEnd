package com.example.BeFETest.BusinessLogicLayer.coin;

import com.example.BeFETest.DTO.SchedulingCoin.SchedulingCoinConverter;
import com.example.BeFETest.DTO.SchedulingCoin.SchedulingCoinDTO;
import com.example.BeFETest.DTO.kosdaq.KosdaqConverter;
import com.example.BeFETest.Entity.SchedulingCoin.SchedulingCoinResponse;
import com.example.BeFETest.Entity.convert.FluctuatingRateUtils;
import com.example.BeFETest.Repository.Coin.Bitcoin.SchedulingCoinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CoinService {

    private final SchedulingCoinRepository repository;

    public List<SchedulingCoinDTO> getTop20Coin(){
        List<SchedulingCoinResponse> top20Data = repository.findTop20ByOrderByIdDesc();
        return top20Data.stream().map(SchedulingCoinConverter::toDto)
                .collect(Collectors.toList());
    }

    public List<SchedulingCoinDTO> getTop20CoinByFluctuating(){
        List<SchedulingCoinResponse> coinResponses = repository.findAll();

        return coinResponses.stream()
                .sorted((a,b) -> {
                    Double rateA = FluctuatingRateUtils.convertFluctuatingRate(a.getFluctuatingRate());
                    Double rateB = FluctuatingRateUtils.convertFluctuatingRate(b.getFluctuatingRate());
                    return rateB.compareTo(rateA);
                })
                .limit(20)
                .map(SchedulingCoinConverter::toDto)
                .collect(Collectors.toList());
    }

    public List<SchedulingCoinDTO> getCoinByFluctuating(){
        List<SchedulingCoinResponse> coinResponses = repository.findAll();

        return coinResponses.stream()
                .sorted((a,b) ->{
                    Double rateA = FluctuatingRateUtils.convertFluctuatingRate(a.getFluctuatingRate());
                    Double rateB = FluctuatingRateUtils.convertFluctuatingRate(b.getFluctuatingRate());
                    return rateB.compareTo(rateA);
                })
                .map(SchedulingCoinConverter::toDto)
                .collect(Collectors.toList());
    }

    public List<SchedulingCoinDTO> getCoinByClosingPrice(){
        List<SchedulingCoinResponse> coinResponses = repository.findAll();

        return coinResponses.stream()
                .sorted((a,b)->{
                    Double priceA = a.getClosingPrice();
                    Double priceB = b.getClosingPrice();
                    return priceB.compareTo(priceA);
                })
                .map(SchedulingCoinConverter::toDto)
                .collect(Collectors.toList());
    }

    public List<SchedulingCoinDTO> getCoinByTradingVolume(){
        List<SchedulingCoinResponse> coinResponses = repository.findAll();

        return coinResponses.stream()
                .sorted((a,b) ->{
                    Double amountA = Double.parseDouble(a.getTradingVolume());
                    Double amountB = Double.parseDouble(b.getTradingVolume());
                    return amountB.compareTo(amountA);
                })
                .map(SchedulingCoinConverter::toDto)
                .collect(Collectors.toList());
    }
}
