package com.example.BeFETest;


import com.example.BeFETest.Entity.BacktestingHistory;
import com.example.BeFETest.Entity.HistoryEntity;
import com.example.BeFETest.Entity.UserEntity;
import com.example.BeFETest.Entity.kosdak.KosdakEntity;
import com.example.BeFETest.Entity.kosdak.KosdakResponse;
import com.example.BeFETest.Entity.kosdak2000.Kosdak2000Entity;
import com.example.BeFETest.Entity.kosdak2000.Kosdak2000Response;
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
    private final Kosdak2000Repository kosdak2000Repository;
    private final Kosdak2000EntityRepository kosdak2000EntityRepository;
    private final UserRepository userRepository; // UserRepository 추가

    public DataInitializer(
            HistoryRepo historyRepo,
            BacktestingHistoryRepository backtestingHistoryRepository,
            KospiRepository kospiRepository,
            KospiEntityRepository kospiEntityRepository,
            KosdakRepository kosdakRepository,
            KosdakEntityRepository kosdakEntityRepository,
            Kosdak2000Repository kosdak2000Repository,
            Kosdak2000EntityRepository kosdak2000EntityRepository,
            UserRepository userRepository // UserRepository 추가
    ) {
        this.historyRepo = historyRepo;
        this.backtestingHistoryRepository = backtestingHistoryRepository;
        this.kospiRepository = kospiRepository;
        this.kospiEntityRepository = kospiEntityRepository;
        this.kosdakRepository = kosdakRepository;
        this.kosdakEntityRepository = kosdakEntityRepository;
        this.kosdak2000Repository = kosdak2000Repository;
        this.kosdak2000EntityRepository = kosdak2000EntityRepository;
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

        // Sample data for Kosdak2000Response
        Kosdak2000Response response3 = new Kosdak2000Response();
        response3.setDate("2024-05-20");
        response3.setCurrentPrice(3000.0);
        response3.setAllDayRatio(0.5);
        response3.setPercentChange(0.02);

        List<Kosdak2000Entity> kosdak2000DataList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Kosdak2000Entity kosdak2000Data = new Kosdak2000Entity();
            kosdak2000Data.setResponse(response3);
            kosdak2000Data.setTime("2024-04-" + (i + 1));
            kosdak2000Data.setValue(3000.0 + i * 10);
            kosdak2000DataList.add(kosdak2000Data);
        }
        response3.setKosdak2000Data(kosdak2000DataList);
        response3 = kosdak2000Repository.save(response3);
        for (Kosdak2000Entity kosdak2000Data : kosdak2000DataList) {
            kosdak2000Data.setResponse(response3);
        }
        kosdak2000EntityRepository.saveAll(kosdak2000DataList);
    }
}

/*
@Component
public class DataInitializer implements CommandLineRunner {

    private final HistoryRepo historyRepo;

    private final BacktestingHistoryRepository backtestingHistoryRepository;

    private final KospiRepository kospiRepository;
    private final KospiEntityRepository kospiEntityRepository; // 변경된 부분

    private final KosdakRepository kosdakRepository;
    private final KosdakEntityRepository kosdakEntityRepository;

    private final Kosdak2000Repository kosdak2000Repository;
    private final Kosdak2000EntityRepository kosdak2000EntityRepository;

    public DataInitializer(HistoryRepo historyRepo, BacktestingHistoryRepository backtestingHistoryRepository, KospiRepository kospiRepository, KospiEntityRepository kospiEntityRepository, KosdakRepository kosdakRepository, KosdakEntityRepository kosdakEntityRepository, Kosdak2000Repository kosdak2000Repository, Kosdak2000EntityRepository kosdak2000EntityRepository) {
        this.historyRepo = historyRepo;
        this.backtestingHistoryRepository = backtestingHistoryRepository;
        this.kospiRepository = kospiRepository;
        this.kospiEntityRepository = kospiEntityRepository;
        this.kosdakRepository = kosdakRepository;
        this.kosdakEntityRepository = kosdakEntityRepository;
        this.kosdak2000Repository = kosdak2000Repository;
        this.kosdak2000EntityRepository = kosdak2000EntityRepository;
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


        Kosdak2000Response response3 = new Kosdak2000Response();
        response3.setDate("2024-05-20");
        response3.setCurrentPrice(3000.0);
        response3.setAllDayRatio(0.5);
        response3.setPercentChange(0.02);


        List<Kosdak2000Entity> kosdak2000DataList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Kosdak2000Entity kosdak2000Data = new Kosdak2000Entity();
            kosdak2000Data.setResponse(response3); // KospiResponse와 연관된 KospiEntity를 설정합니다.
            kosdak2000Data.setTime("2024-04-" + (i + 1));
            kosdak2000Data.setValue(3000.0 + i * 10);
            kosdak2000DataList.add(kosdak2000Data);
        }

        // KospiResponse를 저장합니다.
        response3 = kosdak2000Repository.save(response3);

// KospiEntity를 저장합니다.
        for (Kosdak2000Entity kosdak2000Data : kosdak2000DataList) {
            kosdak2000Data.setResponse(response3); // KospiResponse와 연관된 KospiEntity를 설정합니다.
        }
        kosdak2000DataList = kosdak2000EntityRepository.saveAll(kosdak2000DataList);



    }




}


 */
