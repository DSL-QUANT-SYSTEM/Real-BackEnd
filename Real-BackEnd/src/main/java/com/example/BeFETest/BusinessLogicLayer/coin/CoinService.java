package com.example.BeFETest.BusinessLogicLayer.coin;

import com.example.BeFETest.DTO.SchedulingCoin.SchedulingCoinConverter;
import com.example.BeFETest.DTO.SchedulingCoin.SchedulingCoinDTO;
import com.example.BeFETest.DTO.kosdaq.KosdaqConverter;
import com.example.BeFETest.Entity.SchedulingCoin.SchedulingCoinResponse;
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
}
