package com.example.BeFETest.DTO.coinDTO; // 패키지 경로는 프로젝트에 맞게 수정하세요

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CombinedStrategyDTO {
    private BollingerBandsStrategyDTO bbStrategyDTO;
    private GoldenDeadCrossStrategyDTO goldenStrategyDTO;
    private IndicatorBasedStrategyDTO rsiStrategyDTO;
    private EnvelopeDTO envStrategyDTO;
    private WilliamsDTO williamsStrategyDTO;

    private MultiStrategyDTO multiStrategyDTO; // 공통으로 적용할 MultiStrategyDTO

    // 기본 생성자
    public CombinedStrategyDTO() {}

}
