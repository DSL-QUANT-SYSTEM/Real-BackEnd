package com.example.BeFETest.BusinessLogicLayer.Strategy;

import com.example.BeFETest.DTO.coinDTO.*;
import com.example.BeFETest.Entity.BacktestingRes.BBEntity;
import com.example.BeFETest.Entity.BacktestingRes.EnvEntity;
import com.example.BeFETest.Entity.BacktestingRes.GDEntity;
import com.example.BeFETest.Entity.BacktestingRes.IndicatorEntity;
import com.example.BeFETest.Entity.BacktestingRes.WEntity;
import com.example.BeFETest.Repository.Backtesting.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
public class StrategyService {

    @Autowired
    private GDRepository gdRepository;

    @Autowired
    private BBRepository bbRepository;

    @Autowired
    private IndicatorRepository indicatorRepository;

    @Autowired
    private EnvRepository envRepository;

    @Autowired
    private WRepository wRepository;

    @Transactional
    public void saveGDStrategyResult(StrategyCommonDTO strategyDTO, Long userId, GoldenDeadCrossStrategyDTO gdDTO, GoldenDeadCrossStrategyDTO gdResult){
        //해당 하는 GD entity를 DB에서 userId로 찾은 후 추가 옵션정보 저장하기

        //여기서 GDEntity 처음으로 생성 후 생성자 통해서 다 넣기 DB에서 찾는게 아니라
        //GDEntity gdEntity = gdRepository.findByUserIdOrderByIdDesc(strategyDTO.getUserId()).stream().findFirst().orElse(null);
        //공통정보 저장
        GDEntity gdEntity = new GDEntity();
        gdEntity.setUserId(userId);
        gdEntity.setInitial_investment(strategyDTO.getInitial_investment());
        gdEntity.setTax(strategyDTO.getTax());
        gdEntity.setStart_date(strategyDTO.getStart_date());
        gdEntity.setEnd_date(strategyDTO.getEnd_date());
        gdEntity.setBacktesting_date(LocalDate.now());
        gdEntity.setTarget_item(strategyDTO.getTarget_item());
        gdEntity.setTick_kind(strategyDTO.getTick_kind());
        gdEntity.setInq_range(strategyDTO.getInq_range());
        gdEntity.setStrategy(strategyDTO.getStrategy());
        //골든데드 정보 저장
        gdEntity.setFastMovingAveragePeriod(gdDTO.getFastMoveAvg());
        gdEntity.setSlowMovingAveragePeriod(gdDTO.getSlowMoveAvg());
        //백테스팅 결과 저장
        gdEntity.setFinalAsset(gdResult.getFinalAsset());
        gdEntity.setFinalCash(gdResult.getFinalCash());
        gdEntity.setFinalBalance(gdResult.getFinalBalance());
        gdEntity.setProfit(gdResult.getProfit());
        gdEntity.setProfitRate(gdResult.getProfitRate());
        gdEntity.setNumberOfTrades(gdResult.getNumberOfTrades());

        System.out.println("gdEntity = " + gdEntity.toString());
        gdRepository.save(gdEntity);

        List<GDEntity> gdStrategies = gdRepository.findByUserIdOrderByIdDesc(gdEntity.getUserId());
        if (gdStrategies.size() > 10) {
            List<GDEntity> strategiesToDelete = gdStrategies.subList(10, gdStrategies.size());
            gdRepository.deleteAll(strategiesToDelete);
        }

        //if(gdEntity != null){
        //    gdEntity.setFastMovingAveragePeriod(strategyDTO.getFastMovingAveragePeriod());
        //   gdEntity.setSlowMovingAveragePeriod(strategyDTO.getSlowMovingAveragePeriod());

        //    gdRepository.save(gdEntity);

        //    List<GDEntity> gdStrategies = gdRepository.findByUserIdOrderByIdDesc(gdEntity.getUserId());
        //    if (gdStrategies.size() > 10) {
        //        List<GDEntity> strategiesToDelete = gdStrategies.subList(10, gdStrategies.size());
        //        gdRepository.deleteAll(strategiesToDelete);
        //    }
        //}else {
        //    throw new CustomExceptions.ResourceNotFoundException("전략 데이터 없음", null, "no data in db", ErrorCode.NOT_FOUND);
        //}

    }

    public List<GDEntity> getRecentGDStrategies(Long userId){
        return gdRepository.findTop10ByUserIdOrderByIdDesc(userId);
    }

    public GoldenDeadCrossStrategyDTO getLatestGDStrategyResultByUserId(Long userId){
        GDEntity gdEntity = gdRepository.findTopByUserIdOrderByIdDesc(userId);
        return new GoldenDeadCrossStrategyDTO(gdEntity.getInitial_investment(), gdEntity.getTax(), gdEntity.getStart_date(),
                gdEntity.getEnd_date(), gdEntity.getBacktesting_date(), gdEntity.getTarget_item(), gdEntity.getTick_kind(), gdEntity.getInq_range(),gdEntity.getStrategy(), gdEntity.getFinalCash(), gdEntity.getFinalAsset(),
                gdEntity.getFinalBalance(), gdEntity.getProfit(), gdEntity.getProfitRate(), gdEntity.getNumberOfTrades(), gdEntity.getFastMovingAveragePeriod(), gdEntity.getSlowMovingAveragePeriod());
    }


    @Transactional
    public void saveBBStrategyResult(StrategyCommonDTO strategyDTO, Long userId, BollingerBandsStrategyDTO bbDTO, BollingerBandsStrategyDTO bbResult){

        //공통정보 저장
        BBEntity bbEntity = new BBEntity();
        bbEntity.setUserId(userId);
        bbEntity.setInitial_investment(strategyDTO.getInitial_investment());
        bbEntity.setTax(strategyDTO.getTax());
        bbEntity.setStart_date(strategyDTO.getStart_date());
        bbEntity.setEnd_date(strategyDTO.getEnd_date());
        bbEntity.setBacktesting_date(LocalDate.now());
        bbEntity.setTarget_item(strategyDTO.getTarget_item());
        bbEntity.setTick_kind(strategyDTO.getTick_kind());
        bbEntity.setInq_range(strategyDTO.getInq_range());
        //골든데드 정보 저장
        bbEntity.setMoveAvg(bbDTO.getMoveAvg());
        //백테스팅 결과 저장
        bbEntity.setFinalAsset(bbResult.getFinalAsset());
        bbEntity.setFinalCash(bbResult.getFinalCash());
        bbEntity.setFinalBalance(bbResult.getFinalBalance());
        bbEntity.setProfit(bbResult.getProfit());
        bbEntity.setProfitRate(bbResult.getProfitRate());
        bbEntity.setNumberOfTrades(bbResult.getNumberOfTrades());

        System.out.println("bbEntity = " + bbEntity.toString());
        bbRepository.save(bbEntity);

        List<BBEntity> bbStrategies = bbRepository.findByUserIdOrderByIdDesc(bbEntity.getUserId());
        if (bbStrategies.size() > 10) {
            List<BBEntity> strategiesToDelete = bbStrategies.subList(10, bbStrategies.size());
            bbRepository.deleteAll(strategiesToDelete);
        }
//
//        if(bbEntity != null){
//            bbEntity.setMovingAveragePeriod(strategyDTO.getMovingAveragePeriod());
//
//            bbRepository.save(bbEntity);
//
//            List<BBEntity> bbStrategies = bbRepository.findByUserIdOrderByIdDesc(bbEntity.getUserId());
//            if (bbStrategies.size() > 10) {
//                List<BBEntity> strategiesToDelete = bbStrategies.subList(10, bbStrategies.size());
//                bbRepository.deleteAll(strategiesToDelete);
//            }
//        }else {
//            throw new CustomExceptions.ResourceNotFoundException("전략 데이터 없음", null, "no data in db", ErrorCode.NOT_FOUND);
//        }


    }

    public BollingerBandsStrategyDTO getLatestBBStrategyResultByUserId(Long userId){
        BBEntity bbEntity = bbRepository.findTopByUserIdOrderByIdDesc(userId);
        return new BollingerBandsStrategyDTO(bbEntity.getInitial_investment(), bbEntity.getTax(), bbEntity.getStart_date(),
                bbEntity.getEnd_date(),  bbEntity.getBacktesting_date() , bbEntity.getTarget_item(), bbEntity.getTick_kind(), bbEntity.getInq_range(), bbEntity.getStrategy() ,bbEntity.getFinalCash(), bbEntity.getFinalAsset(),
                bbEntity.getFinalBalance(), bbEntity.getProfit(), bbEntity.getProfitRate(), bbEntity.getNumberOfTrades(),bbEntity.getMoveAvg());
    }

    @Transactional
    public void saveIndicatorStrategyResult(StrategyCommonDTO strategyDTO, Long userId, IndicatorBasedStrategyDTO indDTO, IndicatorBasedStrategyDTO indResult){

        //공통정보 저장
        IndicatorEntity indEntity = new IndicatorEntity();
        indEntity.setUserId(userId);
        indEntity.setInitial_investment(strategyDTO.getInitial_investment());
        indEntity.setTax(strategyDTO.getTax());
        indEntity.setStart_date(strategyDTO.getStart_date());
        indEntity.setEnd_date(strategyDTO.getEnd_date());
        indEntity.setBacktesting_date(LocalDate.now());
        indEntity.setTarget_item(strategyDTO.getTarget_item());
        indEntity.setTick_kind(strategyDTO.getTick_kind());
        indEntity.setInq_range(strategyDTO.getInq_range());
        //골든데드 정보 저장
        indEntity.setRsiPeriod(indDTO.getRsiPeriod());
        //백테스팅 결과 저장
        indEntity.setFinalAsset(indResult.getFinalAsset());
        indEntity.setFinalCash(indResult.getFinalCash());
        indEntity.setFinalBalance(indResult.getFinalBalance());
        indEntity.setProfit(indResult.getProfit());
        indEntity.setProfitRate(indResult.getProfitRate());
        indEntity.setNumberOfTrades(indResult.getNumberOfTrades());

        System.out.println("indicatorEntity = " + indEntity.toString());
        indicatorRepository.save(indEntity);

        List<IndicatorEntity> indStrategies = indicatorRepository.findByUserIdOrderByIdDesc(indEntity.getUserId());
        assert indStrategies != null;
        if (indStrategies.size() > 10) {
            List<IndicatorEntity> strategiesToDelete = indStrategies.subList(10, indStrategies.size());
            indicatorRepository.deleteAll(strategiesToDelete);
        }

//        if(indiEntity != null){
//            indiEntity.setRsiPeriod(strategyDTO.getRsiPeriod());
//
//            indicatorRepository.save(indiEntity);
//
//            List<IndicatorEntity> indiStrategies = indicatorRepository.findByUserIdOrderByIdDesc(indiEntity.getUserId());
//            if (indiStrategies.size() > 10) {
//                List<IndicatorEntity> strategiesToDelete = indiStrategies.subList(10, indiStrategies.size());
//                indicatorRepository.deleteAll(strategiesToDelete);
//            }
//        }else {
//            throw new CustomExceptions.ResourceNotFoundException("전략 데이터 없음", null, "no data in db", ErrorCode.NOT_FOUND);
//        }


    }

    public IndicatorBasedStrategyDTO getLatestIndicatorStrategyResultByUserId(Long userId){
        IndicatorEntity indiEntity = indicatorRepository.findTopByUserIdOrderByIdDesc(userId);
        return new IndicatorBasedStrategyDTO(indiEntity.getInitial_investment(), indiEntity.getTax(), indiEntity.getStart_date(),
                indiEntity.getEnd_date(), indiEntity.getBacktesting_date(), indiEntity.getTarget_item(), indiEntity.getTick_kind(), indiEntity.getInq_range(), indiEntity.getStrategy() ,indiEntity.getFinalCash(), indiEntity.getFinalAsset(),
                indiEntity.getFinalBalance(), indiEntity.getProfit(), indiEntity.getProfitRate(), indiEntity.getNumberOfTrades(),indiEntity.getRsiPeriod());
    }

    @Transactional
    public void saveEnvelopeStrategyResult(StrategyCommonDTO strategyDTO, Long userId, EnvelopeDTO envelopeDTO, EnvelopeDTO envResultDTO){

        //공통정보 저장
        EnvEntity envEntity = new EnvEntity();
        envEntity.setUserId(userId);
        envEntity.setInitial_investment(strategyDTO.getInitial_investment());
        envEntity.setTax(strategyDTO.getTax());
        envEntity.setStart_date(strategyDTO.getStart_date());
        envEntity.setEnd_date(strategyDTO.getEnd_date());
        envEntity.setBacktesting_date(LocalDate.now());
        envEntity.setTarget_item(strategyDTO.getTarget_item());
        envEntity.setTick_kind(strategyDTO.getTick_kind());
        envEntity.setInq_range(strategyDTO.getInq_range());
        //엔벨로프 정보 저장
        envEntity.setMoving_up(envelopeDTO.getMoving_up());
        envEntity.setMoving_down(envelopeDTO.getMoving_down());
        envEntity.setMovingAveragePeriod(envelopeDTO.getMovingAveragePeriod());
        //백테스팅 결과 저장
        envEntity.setFinalAsset(envResultDTO.getFinalAsset());
        envEntity.setFinalCash(envResultDTO.getFinalCash());
        envEntity.setFinalBalance(envResultDTO.getFinalBalance());
        envEntity.setProfit(envResultDTO.getProfit());
        envEntity.setProfitRate(envResultDTO.getProfitRate());
        envEntity.setNumberOfTrades(envResultDTO.getNumberOfTrades());

        System.out.println("envEntity = " + envEntity.toString());
        envRepository.save(envEntity);

        List<EnvEntity> envStrategies = envRepository.findByUserIdOrderByIdDesc(envEntity.getUserId());
        assert envStrategies != null;
        if (envStrategies.size() > 10) {
            List<EnvEntity> strategiesToDelete = envStrategies.subList(10, envStrategies.size());
            envRepository.deleteAll(strategiesToDelete);
        }

//        if(indiEntity != null){
//            indiEntity.setRsiPeriod(strategyDTO.getRsiPeriod());
//
//            indicatorRepository.save(indiEntity);
//
//            List<IndicatorEntity> indiStrategies = indicatorRepository.findByUserIdOrderByIdDesc(indiEntity.getUserId());
//            if (indiStrategies.size() > 10) {
//                List<IndicatorEntity> strategiesToDelete = indiStrategies.subList(10, indiStrategies.size());
//                indicatorRepository.deleteAll(strategiesToDelete);
//            }
//        }else {
//            throw new CustomExceptions.ResourceNotFoundException("전략 데이터 없음", null, "no data in db", ErrorCode.NOT_FOUND);
//        }


    }

    public EnvelopeDTO getLatestEnvStrategyResultByUserId(Long userId){
        EnvEntity envEntity = envRepository.findTopByUserIdOrderByIdDesc(userId);
        return new EnvelopeDTO(envEntity.getInitial_investment(), envEntity.getTax(), envEntity.getStart_date(),
                envEntity.getEnd_date(), envEntity.getBacktesting_date(), envEntity.getTarget_item(), envEntity.getTick_kind(), envEntity.getInq_range(), envEntity.getStrategy() ,envEntity.getFinalCash(), envEntity.getFinalAsset(),
                envEntity.getFinalBalance(), envEntity.getProfit(), envEntity.getProfitRate(), envEntity.getNumberOfTrades(),envEntity.getMoving_up(), envEntity.getMoving_down(), envEntity.getMovingAveragePeriod());
    }

    @Transactional
    public void saveWilliamsStrategyResult(StrategyCommonDTO strategyDTO, Long userId, WilliamsDTO williamsDTO, WilliamsDTO williamsResultDTO){

        //공통정보 저장
        WEntity wEntity = new WEntity();
        wEntity.setUserId(userId);
        wEntity.setInitial_investment(strategyDTO.getInitial_investment());
        wEntity.setTax(strategyDTO.getTax());
        wEntity.setStart_date(strategyDTO.getStart_date());
        wEntity.setEnd_date(strategyDTO.getEnd_date());
        wEntity.setBacktesting_date(LocalDate.now());
        wEntity.setTarget_item(strategyDTO.getTarget_item());
        wEntity.setTick_kind(strategyDTO.getTick_kind());
        wEntity.setInq_range(strategyDTO.getInq_range());
        //골든데드 정보 저장
        wEntity.setWilliamsPeriod(williamsDTO.getWilliamsPeriod());
        //백테스팅 결과 저장
        wEntity.setFinalAsset(williamsResultDTO.getFinalAsset());
        wEntity.setFinalCash(williamsResultDTO.getFinalCash());
        wEntity.setFinalBalance(williamsResultDTO.getFinalBalance());
        wEntity.setProfit(williamsResultDTO.getProfit());
        wEntity.setProfitRate(williamsResultDTO.getProfitRate());
        wEntity.setNumberOfTrades(williamsResultDTO.getNumberOfTrades());

        System.out.println("williamsEntity = " + wEntity.toString());
        wRepository.save(wEntity);

        List<WEntity> wStrategies = wRepository.findByUserIdOrderByIdDesc(wEntity.getUserId());
        assert wStrategies != null;
        if (wStrategies.size() > 10) {
            List<WEntity> strategiesToDelete = wStrategies.subList(10, wStrategies.size());
            wRepository.deleteAll(strategiesToDelete);
        }

//        if(indiEntity != null){
//            indiEntity.setRsiPeriod(strategyDTO.getRsiPeriod());
//
//            indicatorRepository.save(indiEntity);
//
//            List<IndicatorEntity> indiStrategies = indicatorRepository.findByUserIdOrderByIdDesc(indiEntity.getUserId());
//            if (indiStrategies.size() > 10) {
//                List<IndicatorEntity> strategiesToDelete = indiStrategies.subList(10, indiStrategies.size());
//                indicatorRepository.deleteAll(strategiesToDelete);
//            }
//        }else {
//            throw new CustomExceptions.ResourceNotFoundException("전략 데이터 없음", null, "no data in db", ErrorCode.NOT_FOUND);
//        }


    }

    public WilliamsDTO getLatestWilliamsStrategyResultByUserId(Long userId){
        WEntity wEntity = wRepository.findTopByUserIdOrderByIdDesc(userId);
        return new WilliamsDTO(wEntity.getInitial_investment(), wEntity.getTax(), wEntity.getStart_date(),
                wEntity.getEnd_date(),wEntity.getBacktesting_date(), wEntity.getTarget_item(), wEntity.getTick_kind(), wEntity.getInq_range(), wEntity.getStrategy() ,wEntity.getFinalCash(), wEntity.getFinalAsset(),
                wEntity.getFinalBalance(), wEntity.getProfit(), wEntity.getProfitRate(), wEntity.getNumberOfTrades(),wEntity.getWilliamsPeriod());
    }

}
