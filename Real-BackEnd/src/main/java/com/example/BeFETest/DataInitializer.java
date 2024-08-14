package com.example.BeFETest;


import com.example.BeFETest.Entity.kosdak.KosdakResponse;
import com.example.BeFETest.Repository.Kosdak.KosdakRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



//@Component
//public class DataInitializer implements CommandLineRunner {
//
//    @Autowired
//    private KosdakRepository kosdakRepository;
//
//    @Override
//    public void run(String... args) throws Exception {
//        initializeKosdakData();
//    }
//
//    private void initializeKosdakData() {
//
//        kosdakRepository.deleteAll();
//
//        KosdakResponse response1 = new KosdakResponse();
//        response1.setDate("2024-07-01");
//        response1.setClosingPrice(1500.50);
//        response1.setFluctuatingRate(0.68);
//        response1.setOpeningPrice(1480.00);
//        response1.setHighPrice(1520.00);
//        response1.setLowPrice(1470.00);
//        response1.setTradingVolume(500000);
//
//        KosdakResponse response2 = new KosdakResponse();
//        response2.setDate(LocalDate.now().toString());
//        response2.setClosingPrice(1510.70);
//        response2.setComparison(10.20);
//        response2.setFluctuationRate(0.68);
//        response2.setOpeningPrice(1490.00);
//        response2.setHighPrice(1530.00);
//        response2.setLowPrice(1480.00);
//        response2.setTradingVolume(520000);
//        response2.setTradingAmount(780000000);
//        response2.setListedCapitalization(1020000000);
//
//        KosdakResponse response3 = new KosdakResponse();
//        response3.setDate("2024-07-03");
//        response3.setClosingPrice(1520.80);
//        response3.setComparison(10.10);
//        response3.setFluctuationRate(0.67);
//        response3.setOpeningPrice(1500.00);
//        response3.setHighPrice(1540.00);
//        response3.setLowPrice(1490.00);
//        response3.setTradingVolume(530000);
//        response3.setTradingAmount(790000000);
//        response3.setListedCapitalization(1030000000);
//
//        kosdakRepository.saveAll(Arrays.asList(response1, response2, response3));
//
//        System.out.println("KosdakResponse test data initialized");
//        // 기존 데이터 삭제
//        //kosdakRepository.deleteAll();
//
//        /*
//        // 데이터 리스트 생성
//        List<KosdakResponse> kosdakResponses = new ArrayList<>();
//
//        // 첫 번째 데이터 생성
//        KosdakResponse kosdakResponse1 = new KosdakResponse();
//        kosdakResponse1.setDate(LocalDate.now().toString());
//        kosdakResponse1.setCurrentPrice(737000);
//        kosdakResponse1.setAllDayRatio(2.2);
//        kosdakResponse1.setPercentChange(-3.5);
//
//        KosdakEntity kosdakEntity1 = new KosdakEntity();
//        kosdakEntity1.setTime(LocalDateTime.now().toString());
//        kosdakEntity1.setValue(737000);
//        kosdakEntity1.setResponse(kosdakResponse1);
//
//        List<KosdakEntity> kosdakEntities1 = new ArrayList<>();
//        kosdakEntities1.add(kosdakEntity1);
//        kosdakResponse1.setKosdakData(kosdakEntities1);
//
//        kosdakResponses.add(kosdakResponse1);
//
//        // 두 번째 데이터 생성
//        KosdakResponse kosdakResponse2 = new KosdakResponse();
//        kosdakResponse2.setDate(LocalDate.now().minusDays(1).toString());
//        kosdakResponse2.setCurrentPrice(745000);
//        kosdakResponse2.setAllDayRatio(1.8);
//        kosdakResponse2.setPercentChange(2.1);
//
//        KosdakEntity kosdakEntity2 = new KosdakEntity();
//        kosdakEntity2.setTime(LocalDateTime.now().toString());
//        kosdakEntity2.setValue(745000);
//        kosdakEntity2.setResponse(kosdakResponse2);
//
//        List<KosdakEntity> kosdakEntities2 = new ArrayList<>();
//        kosdakEntities2.add(kosdakEntity2);
//        kosdakResponse2.setKosdakData(kosdakEntities2);
//
//        kosdakResponses.add(kosdakResponse2);
//
//        // 세 번째 데이터 생성
//        KosdakResponse kosdakResponse3 = new KosdakResponse();
//        kosdakResponse3.setDate(LocalDate.now().minusDays(2).toString());
//        kosdakResponse3.setCurrentPrice(730000);
//        kosdakResponse3.setAllDayRatio(1.5);
//        kosdakResponse3.setPercentChange(-1.0);
//
//        KosdakEntity kosdakEntity3 = new KosdakEntity();
//        kosdakEntity3.setTime(LocalDateTime.now().toString());
//        kosdakEntity3.setValue(730000);
//        kosdakEntity3.setResponse(kosdakResponse3);
//
//        List<KosdakEntity> kosdakEntities3 = new ArrayList<>();
//        kosdakEntities3.add(kosdakEntity3);
//        kosdakResponse3.setKosdakData(kosdakEntities3);
//
//        kosdakResponses.add(kosdakResponse3);
//
//        // 데이터베이스에 저장
//        kosdakRepository.saveAll(kosdakResponses);
//        */
//
//    }
//}



/*
import com.example.BeFETest.Entity.BacktestingHistory;
import com.example.BeFETest.Entity.HistoryEntity;
import com.example.BeFETest.Entity.UserEntity;
import com.example.BeFETest.Entity.kosdak.KosdakEntity;
import com.example.BeFETest.Entity.kosdak.KosdakResponse;
import com.example.BeFETest.Entity.kospi200.kospi200Entity;
import com.example.BeFETest.Entity.kospi200.kospi200Response;
import com.example.BeFETest.Entity.kospi.KospiEntity;
import com.example.BeFETest.Entity.kospi.KospiResponse;
import com.example.BeFETest.Repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final HistoryRepo historyRepo;
    private final BacktestingHistoryRepository backtestingHistoryRepository;
    private final KospiRepository kospiRepository;
    private final KospiEntityRepository kospiEntityRepository;
    private final KosdakRepository kosdakRepository;
    private final KosdakEntityRepository kosdakEntityRepository;
    private final kospi200Repository kospi200Repository;
    private final kospi200EntityRepository kospi200EntityRepository;
    private final UserRepository userRepository; // UserRepository 추가

    public DataInitializer(
            HistoryRepo historyRepo,
            BacktestingHistoryRepository backtestingHistoryRepository,
            KospiRepository kospiRepository,
            KospiEntityRepository kospiEntityRepository,
            KosdakRepository kosdakRepository,
            KosdakEntityRepository kosdakEntityRepository,
            kospi200Repository kospi200Repository,
            kospi200EntityRepository kospi200EntityRepository,
            UserRepository userRepository // UserRepository 추가
    ) {
        this.historyRepo = historyRepo;
        this.backtestingHistoryRepository = backtestingHistoryRepository;
        this.kospiRepository = kospiRepository;
        this.kospiEntityRepository = kospiEntityRepository;
        this.kosdakRepository = kosdakRepository;
        this.kosdakEntityRepository = kosdakEntityRepository;
        this.kospi200Repository = kospi200Repository;
        this.kospi200EntityRepository = kospi200EntityRepository;
        this.userRepository = userRepository; // UserRepository 추가
    }

    @Override
    public void run(String... args) throws Exception {
        // 샘플 유저 데이터 생성
        UserEntity user = new UserEntity();
        user.setName("최승아");
        user.setPhoneNumber("010-7110-0441");
        user.setBirthDate("010418");
        user.setGender("여자");
        user.setRecords(Arrays.asList("기록1", "기록2"));

        // UserRepository에 저장
        userRepository.save(user);

        // 나머지 샘플 데이터 생성 (기존 코드)
        HistoryEntity testData = new HistoryEntity();
        testData.setDate("2024-04-14");
        testData.setStock("ABC");
        testData.setClosePrice(100.0);
        testData.setOpenPrice(110.0);
        testData.setHighestPrice(120.0);
        testData.setLowestPrice(90.0);
        testData.setAdjClosePrice(95.0);
        testData.setVolume(10000.0);

        // Repository에 저장
        historyRepo.save(testData);

        List<BacktestingHistory> testDataList = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            BacktestingHistory testData2 = new BacktestingHistory();
            testData2.setDate("2024-04-" + (i + 1));
            testData2.setUniverse("Universe " + (i + 1));
            testData2.setWeight(0.1 * (i + 1));
            testData2.setInitialInvestment(1000.0 * (i + 1));
            testData2.setPeriod(30 + (i % 3)); // 30, 31, 32, 30, 31, ...
            testData2.setFileHtml("backtesting" + (i + 1) + ".html");
            testDataList.add(testData2);
        }
        backtestingHistoryRepository.saveAll(testDataList);

        // Sample data for KospiResponse
        KospiResponse response = new KospiResponse();
        response.setDate("2024-05-20");
        response.setCurrentPrice(3000.0);
        response.setAllDayRatio(0.5);
        response.setPercentChange(0.02);

        List<KospiEntity> kospiDataList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            KospiEntity kospiData = new KospiEntity();
            kospiData.setResponse(response);
            kospiData.setTime("2024-05-" + (i + 1));
            kospiData.setValue(3000.0 + i * 10);
            kospiDataList.add(kospiData);
        }
        response.setKospiData(kospiDataList);
        response = kospiRepository.save(response);
        for (KospiEntity kospiData : kospiDataList) {
            kospiData.setResponse(response);
        }
        kospiEntityRepository.saveAll(kospiDataList);

        // Sample data for KosdakResponse
        KosdakResponse response2 = new KosdakResponse();
        response2.setDate("2024-05-19");
        response2.setCurrentPrice(3000.0);
        response2.setAllDayRatio(0.5);
        response2.setPercentChange(0.02);

        List<KosdakEntity> kosdakDataList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            KosdakEntity kosdakData = new KosdakEntity();
            kosdakData.setResponse(response2);
            kosdakData.setTime("2024-04-" + (i + 1));
            kosdakData.setValue(3000.0 + i * 10);
            kosdakDataList.add(kosdakData);
        }
        response2.setKosdakData(kosdakDataList);
        response2 = kosdakRepository.save(response2);
        for (KosdakEntity kosdakData : kosdakDataList) {
            kosdakData.setResponse(response2);
        }
        kosdakEntityRepository.saveAll(kosdakDataList);

        // Sample data for kospi200Response
        kospi200Response response3 = new kospi200Response();
        response3.setDate("2024-05-20");
        response3.setCurrentPrice(3000.0);
        response3.setAllDayRatio(0.5);
        response3.setPercentChange(0.02);

        List<kospi200Entity> kospi200DataList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            kospi200Entity kospi200Data = new kospi200Entity();
            kospi200Data.setResponse(response3);
            kospi200Data.setTime("2024-04-" + (i + 1));
            kospi200Data.setValue(3000.0 + i * 10);
            kospi200DataList.add(kospi200Data);
        }
        response3.setkospi200Data(kospi200DataList);
        response3 = kospi200Repository.save(response3);
        for (kospi200Entity kospi200Data : kospi200DataList) {
            kospi200Data.setResponse(response3);
        }
        kospi200EntityRepository.saveAll(kospi200DataList);
    }
}
*/

/*
@Component
public class DataInitializer implements CommandLineRunner {

    private final HistoryRepo historyRepo;

    private final BacktestingHistoryRepository backtestingHistoryRepository;

    private final KospiRepository kospiRepository;
    private final KospiEntityRepository kospiEntityRepository; // 변경된 부분

    private final KosdakRepository kosdakRepository;
    private final KosdakEntityRepository kosdakEntityRepository;

    private final kospi200Repository kospi200Repository;
    private final kospi200EntityRepository kospi200EntityRepository;

    public DataInitializer(HistoryRepo historyRepo, BacktestingHistoryRepository backtestingHistoryRepository, KospiRepository kospiRepository, KospiEntityRepository kospiEntityRepository, KosdakRepository kosdakRepository, KosdakEntityRepository kosdakEntityRepository, kospi200Repository kospi200Repository, kospi200EntityRepository kospi200EntityRepository) {
        this.historyRepo = historyRepo;
        this.backtestingHistoryRepository = backtestingHistoryRepository;
        this.kospiRepository = kospiRepository;
        this.kospiEntityRepository = kospiEntityRepository;
        this.kosdakRepository = kosdakRepository;
        this.kosdakEntityRepository = kosdakEntityRepository;
        this.kospi200Repository = kospi200Repository;
        this.kospi200EntityRepository = kospi200EntityRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // 샘플 데이터 생성
        HistoryEntity testData = new HistoryEntity();
        testData.setDate("2024-04-14");
        testData.setStock("ABC");
        testData.setClosePrice(100.0);
        testData.setOpenPrice(110.0);
        testData.setHighestPrice(120.0);
        testData.setLowestPrice(90.0);
        testData.setAdjClosePrice(95.0);
        testData.setVolume(10000.0);

        // Repository에 저장
        historyRepo.save(testData);

        List<BacktestingHistory> testDataList = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            BacktestingHistory testData2 = new BacktestingHistory();
            testData2.setDate("2024-04-" + (i + 1));
            testData2.setUniverse("Universe " + (i + 1));
            testData2.setWeight(0.1 * (i + 1));
            testData2.setInitialInvestment(1000.0 * (i + 1));
            testData2.setPeriod(30 + (i % 3)); // 30, 31, 32, 30, 31, ...
            testData2.setFileHtml("backtesting" + (i + 1) + ".html");
            testDataList.add(testData2);
        }
        backtestingHistoryRepository.saveAll(testDataList);

        /*
        // Sample data for KospiResponse
        KospiResponse response = new KospiResponse();
        response.setDate("2024-04-14");
        response.setCurrentPrice(3000.0);
        response.setAllDayRatio(0.5);
        response.setPercentChange(0.02);

        List<KospiEntity> kospiDataList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            KospiEntity kospiData = new KospiEntity();
            kospiData.setResponse(response);
            kospiData.setTime("2024-04-" + (i + 1));
            kospiData.setValue(3000.0 + i * 10);
            kospiDataList.add(kospiData);
        }
        response.setKospiData(kospiDataList);
        kospiRepository.save(response);

         */

/*
        KospiResponse response = new KospiResponse();
        response.setDate("2024-05-20");
        response.setCurrentPrice(3000.0);
        response.setAllDayRatio(0.5);
        response.setPercentChange(0.02);


        List<KospiEntity> kospiDataList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            KospiEntity kospiData = new KospiEntity();
            kospiData.setResponse(response); // KospiResponse와 연관된 KospiEntity를 설정합니다.
            kospiData.setTime("2024-05-" + (i + 1));
            kospiData.setValue(3000.0 + i * 10);
            kospiDataList.add(kospiData);
        }

        // KospiResponse를 저장합니다.
        response = kospiRepository.save(response);

// KospiEntity를 저장합니다.
        for (KospiEntity kospiData : kospiDataList) {
            kospiData.setResponse(response); // KospiResponse와 연관된 KospiEntity를 설정합니다.
        }
        kospiDataList = kospiEntityRepository.saveAll(kospiDataList);




        KosdakResponse response2 = new KosdakResponse();
        response2.setDate("2024-05-19");
        response2.setCurrentPrice(3000.0);
        response2.setAllDayRatio(0.5);
        response2.setPercentChange(0.02);


        List<KosdakEntity> kosdakDataList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            KosdakEntity kosdakData = new KosdakEntity();
            kosdakData.setResponse(response2); // KospiResponse와 연관된 KospiEntity를 설정합니다.
            kosdakData.setTime("2024-04-" + (i + 1));
            kosdakData.setValue(3000.0 + i * 10);
            kosdakDataList.add(kosdakData);
        }

        // KospiResponse를 저장합니다.
        response2 = kosdakRepository.save(response2);


        for (KosdakEntity kosdakData : kosdakDataList) {
            kosdakData.setResponse(response2); // KospiResponse와 연관된 KospiEntity를 설정합니다.
        }
        kosdakDataList = kosdakEntityRepository.saveAll(kosdakDataList);


        kospi200Response response3 = new kospi200Response();
        response3.setDate("2024-05-20");
        response3.setCurrentPrice(3000.0);
        response3.setAllDayRatio(0.5);
        response3.setPercentChange(0.02);


        List<kospi200Entity> kospi200DataList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            kospi200Entity kospi200Data = new kospi200Entity();
            kospi200Data.setResponse(response3); // KospiResponse와 연관된 KospiEntity를 설정합니다.
            kospi200Data.setTime("2024-04-" + (i + 1));
            kospi200Data.setValue(3000.0 + i * 10);
            kospi200DataList.add(kospi200Data);
        }

        // KospiResponse를 저장합니다.
        response3 = kospi200Repository.save(response3);

// KospiEntity를 저장합니다.
        for (kospi200Entity kospi200Data : kospi200DataList) {
            kospi200Data.setResponse(response3); // KospiResponse와 연관된 KospiEntity를 설정합니다.
        }
        kospi200DataList = kospi200EntityRepository.saveAll(kospi200DataList);



    }




}


 */
