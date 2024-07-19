package com.example.BeFETest.BusinessLogicLayer.Strategy;

import com.example.BeFETest.DTO.coinDTO.GoldenDeadCrossStrategyDTO;
import com.example.BeFETest.DTO.coinDTO.StrategyCommonDTO;
import com.example.BeFETest.Entity.BacktestingRes.GDEntity;
import com.example.BeFETest.Error.CustomExceptions;
import com.example.BeFETest.Error.ErrorCode;
import com.example.BeFETest.Repository.Backtesting.GDRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StrategyService {

    @Autowired
    private GDRepository gdRepository;

    @Transactional
    public void saveCommonStrategyResult(StrategyCommonDTO strategyDTO, Long userId){
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

    @Transactional
    public void saveGDStrategyResult(GoldenDeadCrossStrategyDTO strategyDTO){
        //해당 하는 GD entity를 DB에서 userId로 찾은 후 추가 옵션정보 저장하기

        GDEntity gdEntity = gdRepository.findByUserIdOrderByIdDesc(strategyDTO.getUserId()).stream().findFirst().orElse(null);

        if(gdEntity != null){
            gdEntity.setFastMovingAveragePeriod(strategyDTO.getFastMovingAveragePeriod());
            gdEntity.setSlowMovingAveragePeriod(strategyDTO.getSlowMovingAveragePeriod());

            gdRepository.save(gdEntity);

            List<GDEntity> gdStrategies = gdRepository.findByUserIdOrderByIdDesc(gdEntity.getUserId());
            if (gdStrategies.size() > 10) {
                List<GDEntity> strategiesToDelete = gdStrategies.subList(10, gdStrategies.size());
                gdRepository.deleteAll(strategiesToDelete);
            }
        }else {
            throw new CustomExceptions.ResourceNotFoundException("전략 데이터 없음", null, "no data in db", ErrorCode.NOT_FOUND);
        }


    }

    public List<GDEntity> getRecentGDStrategies(Long userId){
        return gdRepository.findTop10ByUserIdOrderByIdDesc(userId);
    }



}
