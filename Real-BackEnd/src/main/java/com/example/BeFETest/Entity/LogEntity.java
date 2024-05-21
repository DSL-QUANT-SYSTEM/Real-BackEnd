package com.example.BeFETest.Entity;


import com.example.BeFETest.DTO.coinDTO.logInDTO;
import com.example.BeFETest.DTO.coinDTO.logOutDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class LogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String date;
    @Column
    private String symbol;
    @Column
    private double signalLog;
    @Column
    private double amount;
    @Column
    private double price;
    @Column
    private String logNameId;

    public LogEntity(logInDTO inDTO, logOutDTO outDTO ){
        this.date = inDTO.getDate();
        this.symbol = inDTO.getSymbol();
        this.signalLog = outDTO.getSignal();
        this.amount = outDTO.getAmount();
        this.price = outDTO.getPrice();
        this.logNameId = outDTO.getLog_name_id();
    }

}
