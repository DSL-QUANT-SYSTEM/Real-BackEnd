package com.example.BeFETest.entity;

/*
import com.example.BeFETest.DTO.coinDTO.historyInDTO;
import com.example.BeFETest.DTO.coinDTO.historyOutDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class HistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String stock;
    @Column
    private String date;
    @Column
    private double closePrice;
    @Column
    private double openPrice;
    @Column
    private double highestPrice;
    @Column
    private double lowestPrice;
    @Column
    private double adjClosePrice;
    @Column
    private double volume;

    public HistoryEntity(historyInDTO inDTO, historyOutDTO outDTO){
        this.stock = inDTO.getStock();
        this.date = inDTO.getDate();
        this.closePrice = outDTO.getClosePrice();
        this.openPrice = outDTO.getOpenPrice();
        this.highestPrice = outDTO.getHighestPrice();
        this.lowestPrice = outDTO.getLowestPrice();
        this.adjClosePrice =outDTO.getAdjClosePrice();
        this.volume = outDTO.getVolume();
    }


//    public void addTestDataToRepository(HistoryRepo historyRepo) {
//        // 샘플 데이터 생성
//        HistoryEntity testData = new HistoryEntity();
//        testData.setDate("2024-04-14");
//        testData.setStock("ABC");
//        testData.setClosePrice(100.0);
//        testData.setOpenPrice(110.0);
//        testData.setHighestPrice(120.0);
//        testData.setLowestPrice(90.0);
//        testData.setAdjClosePrice(95.0);
//        testData.setVolume(10000.0);
//
//        // Repository에 저장
//        historyRepo.save(testData);
//    }
}

 */
