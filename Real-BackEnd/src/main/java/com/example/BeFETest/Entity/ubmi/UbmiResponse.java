package com.example.BeFETest.Entity.ubmi;

import com.example.BeFETest.Entity.ubai.UbaiEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "ubmi_response")
public class UbmiResponse {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String date;

    @Column(name = "current_price", nullable = false)
    private double currentPrice;

    @Column(name = "allday_ratio", nullable = false)
    private double allDayRatio;

    @Column(name = "percent_change", nullable = false)
    private double percentChange;

    @OneToMany(mappedBy = "response", cascade = CascadeType.ALL)
    private List<UbmiEntity> ubmiData;


}
