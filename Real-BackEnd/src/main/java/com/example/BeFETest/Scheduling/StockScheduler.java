package com.example.BeFETest.Scheduling;

import com.example.BeFETest.Entity.kosdaq.KosdaqResponse;
import com.example.BeFETest.Repository.Kosdaq.KosdaqRepository;
import com.example.BeFETest.Repository.Kospi.KospiRepository;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@EnableScheduling
@Configuration
@RequiredArgsConstructor
public class StockScheduler {
    private KosdaqRepository kosdakRepo;

    private KospiRepository kospiRepo;

    @Scheduled(fixedRate = 500000)
    public void testScheduler(){
        try{
            Document kospi = Jsoup.connect("https://finance.naver.com/sise/sise_index.naver?code=KOSPI").get();
            Document kosdaq = Jsoup.connect("https://finance.naver.com/sise/sise_index.naver?code=KOSDAQ").get();

            String nowValue = "#now_value";

            Elements kospiElement = kospi.select(nowValue);
            Elements kosdaqElement = kosdaq.select(nowValue);
//            System.out.println(kospiElement.text());
//            System.out.println(kosdaqElement.text());

            List<KosdaqResponse> kosdakList = new ArrayList<>();

//            kosdakResponse.setDate(LocalDate.now().toString());
//            kosdakResponse.setHighPrice(kosdaqElement.text());
//            kosdakRepo.saveAll(kosdakResponse);


            //double CurrentPrice = Double.parseDouble(kosdaqElement.text());
            //kosdakResponse1.setDate(LocalDate.now().toString());
            //kosdakResponse1.setCurrentPrice(CurrentPrice);
            //kosdakResponse1.setPercentChange(-3.5);
            //kosdakResponses.add(kosdakResponse1);

            //kosdakRepo.saveAll(kosdakResponses);

            //kospiList.add(createKospiDTO(kospiElement));

        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
