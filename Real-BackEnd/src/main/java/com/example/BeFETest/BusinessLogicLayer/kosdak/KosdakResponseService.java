package com.example.BeFETest.BusinessLogicLayer.kosdak;


import com.example.BeFETest.DTO.kosdak.KosdakConverter;
import com.example.BeFETest.DTO.kosdak.KosdakResponseDTO;
import com.example.BeFETest.Entity.kosdak.KosdakResponse;
import com.example.BeFETest.Repository.Kosdak.KosdakRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class KosdakResponseService {

    @Autowired
    private KosdakRepository kosdakRepository;

//    public List<KosdakResponseDTO> getResponsesByYear(){
//        LocalDate today = LocalDate.now();
//        LocalDate oneYearMinus = LocalDate.now().minusYears(1);
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        String formatDate = oneYearMinus.format(formatter);
//        String endDate = today.format(formatter);
//        log.info("FORMAT DATE: {}, CURRENT DATE: {}", formatDate, endDate);
//
//        List<KosdakResponse> kosdakResponses = kosdakRepository.findResponsesByDate(formatDate);
//        //List<KosdakResponse> kosdakResponses = kosdakRepository.findResponsesWithinDateRange(formatDate, endDate);
//        return kosdakResponses.stream()
//                .map(KosdakConverter::toDto)
//                .collect(Collectors.toList());
//
//    }
}
