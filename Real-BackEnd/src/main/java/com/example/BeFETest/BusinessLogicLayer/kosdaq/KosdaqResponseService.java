package com.example.BeFETest.BusinessLogicLayer.kosdaq;


import com.example.BeFETest.DTO.kosdaq.KosdaqConverter;
import com.example.BeFETest.DTO.kosdaq.KosdaqResponseDTO;
import com.example.BeFETest.Entity.kosdaq.KosdaqResponse;
import com.example.BeFETest.Repository.Kosdaq.KosdaqRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class KosdaqResponseService {

    @Autowired
    private KosdaqRepository kosdaqRepository;

    public List<KosdaqResponseDTO> getResponsesByYear() {

        LocalDate today = LocalDate.now();
        LocalDate oneYearMinus = LocalDate.now().minusYears(1);
        List<KosdaqResponse> responsesData = kosdaqRepository.findByDateBetween(oneYearMinus, today);
        return responsesData.stream().map(KosdaqConverter::toDto)
                .collect(Collectors.toList());
    }
}
