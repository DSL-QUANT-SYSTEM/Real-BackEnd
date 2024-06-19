package com.example.BeFETest.DTO.coinDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Repository;

@Repository
public class logInDTO{


    private String date;

    private String symbol;

    public String toJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            return objectMapper.writeValueAsString(this);
        } catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public String getDate(){
        return this.date;
    }
    public String getSymbol(){
        return this.symbol;
    }

    public void setDate(String dateInput){
        this.date = dateInput;
    }
    public void setSymbol(String  symbolInput){
        this.symbol = symbolInput;
    }


}