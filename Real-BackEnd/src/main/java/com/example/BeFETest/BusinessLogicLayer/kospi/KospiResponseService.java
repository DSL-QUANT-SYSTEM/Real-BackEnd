package com.example.BeFETest.BusinessLogicLayer.kospi;


import com.example.BeFETest.DTO.kospi.KospiConverter;
import com.example.BeFETest.DTO.kospi.KospiResponseDTO;
import com.example.BeFETest.Entity.kospi.KospiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.BeFETest.Repository.Kospi.KospiRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class KospiResponseService {

    @Autowired
    private KospiRepository KospiRepository;

    public List<KospiResponseDTO> getResponsesByYear(){
        LocalDate today = LocalDate.now();
        LocalDate oneYearMinus = LocalDate.now().minusYears(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formatDate = oneYearMinus.format(formatter);
        String endDate = today.format(formatter);
        log.info("FORMAT DATE: {}, CURRENT DATE: {}", formatDate, endDate);

        List<KospiResponse> KospiResponses = KospiRepository.findResponsesByDate(formatDate);
        //List<KospiResponse> KospiResponses = KospiRepository.findResponsesWithinDateRange(formatDate, endDate);
        return KospiResponses.stream()
                .map(KospiConverter::toDto)
                .collect(Collectors.toList());

    }
}
