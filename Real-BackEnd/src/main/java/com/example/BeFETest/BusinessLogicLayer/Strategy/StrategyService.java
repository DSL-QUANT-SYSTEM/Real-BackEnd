package com.example.BeFETest.BusinessLogicLayer.Strategy;

import com.example.BeFETest.DTO.coinDTO.BollingerBandsStrategyDTO;
import com.example.BeFETest.DTO.coinDTO.GoldenDeadCrossStrategyDTO;
import com.example.BeFETest.DTO.coinDTO.IndicatorBasedStrategyDTO;
import com.example.BeFETest.DTO.coinDTO.StrategyCommonDTO;
import com.example.BeFETest.Entity.BacktestingRes.BBEntity;
import com.example.BeFETest.Entity.BacktestingRes.GDEntity;
import com.example.BeFETest.Entity.BacktestingRes.IndicatorEntity;
import com.example.BeFETest.Error.CustomExceptions;
import com.example.BeFETest.Error.ErrorCode;
import com.example.BeFETest.Repository.Backtesting.BBRepository;
import com.example.BeFETest.Repository.Backtesting.GDRepository;
import com.example.BeFETest.Repository.Backtesting.IndicatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StrategyService {

    @Autowired
    private GDRepository gdRepository;

    @Autowired
    private BBRepository bbRepository;

    @Autowired
    private IndicatorRepository indicatorRepository;

    @Transactional
    public void saveGDCommonStrategyResult(StrategyCommonDTO strategyDTO, Long userId){
        GDEntity gdEntity = new GDEntity();
        gdEntity.setUserId(userId);
        gdEntity.setInitialInvestment(strategyDTO.getInitialInvestment());
        gdEntity.setTransactionFee(strategyDTO.getTransactionFee());
        gdEntity.setStartDate(strategyDTO.getStartDate());
        gdEntity.setEndDate(strategyDTO.getEndDate());
        gdEntity.setTickKind(strategyDTO.getTickKind());
        gdEntity.setInquiryRange(strategyDTO.getInquiryRange());

        gdRepository.save(gdEntity);

        List<GDEntity> gdStrategies = gdRepository.findByUserIdOrderByIdDesc(gdEntity.getUserId());
        if(gdStrategies.size() > 10){
            List<GDEntity> strategiesToDelete = gdStrategies.subList(10, gdStrategies.size());
            gdRepository.deleteAll(strategiesToDelete);
        }
    }


    public StrategyCommonDTO saveCommonStrategyResult(StrategyCommonDTO strategyDTO){
        StrategyCommonDTO commonDTO = new StrategyCommonDTO(strategyDTO.getInitialInvestment(), strategyDTO.getTransactionFee(), strategyDTO.getStartDate(),
                strategyDTO.getEndDate(), strategyDTO.getTargetItem(), strategyDTO.getTargetItem(), strategyDTO.getInquiryRange());

        return commonDTO;
    }



    @Transactional
    public void saveGDStrategyResult(StrategyCommonDTO strategyDTO, Long userId, GoldenDeadCrossStrategyDTO gdDTO){
        //해당 하는 GD entity를 DB에서 userId로 찾은 후 추가 옵션정보 저장하기

        //여기서 GDEntity 처음으로 생성 후 생성자 통해서 다 넣기 DB에서 찾는게 아니라
        //GDEntity gdEntity = gdRepository.findByUserIdOrderByIdDesc(strategyDTO.getUserId()).stream().findFirst().orElse(null);
        GDEntity gdEntity = new GDEntity();
        gdEntity.setUserId(userId);
        gdEntity.setInitialInvestment(strategyDTO.getInitialInvestment());
        gdEntity.setTransactionFee(strategyDTO.getTransactionFee());
        gdEntity.setStartDate(strategyDTO.getStartDate());
        gdEntity.setEndDate(strategyDTO.getEndDate());
        gdEntity.setTargetItem(strategyDTO.getTargetItem());
        gdEntity.setTickKind(strategyDTO.getTickKind());
        gdEntity.setInquiryRange(strategyDTO.getInquiryRange());

        gdEntity.setFastMovingAveragePeriod(gdDTO.getFastMovingAveragePeriod());
        gdEntity.setSlowMovingAveragePeriod(gdDTO.getSlowMovingAveragePeriod());

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
        return new GoldenDeadCrossStrategyDTO(gdEntity.getInitialInvestment(), gdEntity.getTransactionFee(), gdEntity.getStartDate(),
                gdEntity.getEndDate(), gdEntity.getTargetItem(), gdEntity.getTickKind(), gdEntity.getInquiryRange(), gdEntity.getFinalCash(), gdEntity.getFinalAsset(),
                gdEntity.getFinalBalance(), gdEntity.getProfit(), gdEntity.getProfitRate(), gdEntity.getNumberOfTrades(), gdEntity.getFastMovingAveragePeriod(), gdEntity.getSlowMovingAveragePeriod());
    }


    @Transactional
    public void saveBBStrategyResult(BollingerBandsStrategyDTO strategyDTO){

        BBEntity bbEntity = bbRepository.findByUserIdOrderByIdDesc(strategyDTO.getUserId()).stream().findFirst().orElse(null);

        if(bbEntity != null){
            bbEntity.setMovingAveragePeriod(strategyDTO.getMovingAveragePeriod());

            bbRepository.save(bbEntity);

            List<BBEntity> bbStrategies = bbRepository.findByUserIdOrderByIdDesc(bbEntity.getUserId());
            if (bbStrategies.size() > 10) {
                List<BBEntity> strategiesToDelete = bbStrategies.subList(10, bbStrategies.size());
                bbRepository.deleteAll(strategiesToDelete);
            }
        }else {
            throw new CustomExceptions.ResourceNotFoundException("전략 데이터 없음", null, "no data in db", ErrorCode.NOT_FOUND);
        }


    }

    public BollingerBandsStrategyDTO getLatestBBStrategyResultByUserId(Long userId){
        BBEntity bbEntity = bbRepository.findTopByUserIdOrderByIdDesc(userId);
        return new BollingerBandsStrategyDTO(bbEntity.getInitialInvestment(), bbEntity.getTransactionFee(), bbEntity.getStartDate(),
                bbEntity.getEndDate(), bbEntity.getTargetItem(), bbEntity.getTickKind(), bbEntity.getInquiryRange(), bbEntity.getFinalCash(), bbEntity.getFinalAsset(),
                bbEntity.getFinalBalance(), bbEntity.getProfit(), bbEntity.getProfitRate(), bbEntity.getNumberOfTrades(),bbEntity.getMovingAveragePeriod());
    }

    @Transactional
    public void saveIndicatorStrategyResult(IndicatorBasedStrategyDTO strategyDTO){

        IndicatorEntity indiEntity = indicatorRepository.findByUserIdOrderByIdDesc(strategyDTO.getUserId()).stream().findFirst().orElse(null);

        if(indiEntity != null){
            indiEntity.setRsiPeriod(strategyDTO.getRsiPeriod());

            indicatorRepository.save(indiEntity);

            List<IndicatorEntity> indiStrategies = indicatorRepository.findByUserIdOrderByIdDesc(indiEntity.getUserId());
            if (indiStrategies.size() > 10) {
                List<IndicatorEntity> strategiesToDelete = indiStrategies.subList(10, indiStrategies.size());
                indicatorRepository.deleteAll(strategiesToDelete);
            }
        }else {
            throw new CustomExceptions.ResourceNotFoundException("전략 데이터 없음", null, "no data in db", ErrorCode.NOT_FOUND);
        }


    }

    public IndicatorBasedStrategyDTO getLatestIndicatorStrategyResultByUserId(Long userId){
        IndicatorEntity indiEntity = indicatorRepository.findTopByUserIdOrderByIdDesc(userId);
        return new IndicatorBasedStrategyDTO(indiEntity.getInitialInvestment(), indiEntity.getTransactionFee(), indiEntity.getStartDate(),
                indiEntity.getEndDate(), indiEntity.getTargetItem(), indiEntity.getTickKind(), indiEntity.getInquiryRange(), indiEntity.getFinalCash(), indiEntity.getFinalAsset(),
                indiEntity.getFinalBalance(), indiEntity.getProfit(), indiEntity.getProfitRate(), indiEntity.getNumberOfTrades(),indiEntity.getRsiPeriod());
    }

}
