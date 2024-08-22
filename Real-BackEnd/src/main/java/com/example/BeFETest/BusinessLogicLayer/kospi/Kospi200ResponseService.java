package com.example.BeFETest.BusinessLogicLayer.kospi;


import com.example.BeFETest.DTO.kospi200.Kospi200Converter;
import com.example.BeFETest.DTO.kospi200.Kospi200ResponseDTO;
import com.example.BeFETest.Entity.kospi200.Kospi200Response;
import com.example.BeFETest.Repository.Kospi.Kospi200Repository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class Kospi200ResponseService {

    @Autowired
    private Kospi200Repository Kospi200Repository;

    public List<Kospi200ResponseDTO> getResponsesByYear(){
        LocalDate today = LocalDate.now();
        LocalDate oneYearMinus = LocalDate.now().minusYears(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formatDate = oneYearMinus.format(formatter);
        String endDate = today.format(formatter);
        log.info("FORMAT DATE: {}, CURRENT DATE: {}", formatDate, endDate);

        List<Kospi200Response> Kospi200Responses = Kospi200Repository.findResponsesByDate(formatDate);
        //List<Kospi200Response> Kospi200Responses = Kospi200Repository.findResponsesWithinDateRange(formatDate, endDate);
        return Kospi200Responses.stream()
                .map(Kospi200Converter::toDto)
                .collect(Collectors.toList());

    }
}
