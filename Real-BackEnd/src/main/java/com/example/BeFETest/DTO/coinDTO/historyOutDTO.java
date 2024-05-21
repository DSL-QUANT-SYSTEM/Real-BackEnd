package com.example.BeFETest.DTO.coinDTO;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Repository;

@Repository
public class historyOutDTO {

    private double closePrice;
    private double openPrice;
    private double highestPrice;
    private double lowestPrice;
    private double adjClosePrice;
    private double volume;


    /*
    public String getDataFromDatabase() {
        // DAO3에서는 데이터베이스와 상호 작용을 위한 로직을 구현합니다.
        return "Data from Database (Dao1)";
    }
    */

    //데이터 객체 JSON데이터 형식으로 변환 위한 함수
    public String toJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            return objectMapper.writeValueAsString(this);
        } catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public double getClosePrice(){
        return this.closePrice;
    }


    public double getOpenPrice(){
        return this.openPrice;
    }


    public double getHighestPrice(){
        return this.highestPrice;
    }


    public double getLowestPrice(){
        return this.lowestPrice;
    }

    public double getAdjClosePrice() { return this.adjClosePrice; }

    public double getVolume() { return this.volume;}

    public void setClosePrice(double closePriceInput){
        this.closePrice = closePriceInput;
    }


    public void setOpenPrice(double openPriceInput){
        this.openPrice = openPriceInput;
    }

    public void setHighestPrice(double highestPriceInput){
        this.highestPrice = highestPriceInput;
    }


    public void setLowestPrice(double lowestPriceInput){
        this.lowestPrice = lowestPriceInput;
    }


    public void setAdjClosePrice(double adjClosePriceInput){
        this.adjClosePrice=adjClosePriceInput;
    }

    public void setVolume(double volumeInput){
        this.volume=volumeInput;
    }


}
