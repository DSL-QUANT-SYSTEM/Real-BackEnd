package com.example.BeFETest.BusinessLogicLayer.kosdaq;


import com.example.BeFETest.Repository.Kosdaq.KosdaqRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KosdaqResponseService {

    @Autowired
    private KosdaqRepository kosdaqRepository;

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
