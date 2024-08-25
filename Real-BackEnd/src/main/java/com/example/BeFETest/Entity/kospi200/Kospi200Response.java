package com.example.BeFETest.Entity.kospi200;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "kospi200_response")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Kospi200Response {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @DateTimeFormat(pattern = "yyyy- MM- dd")
    private LocalDate date;

    private String closingPrice;

    private String openingPrice;

    private String highPrice;

    private String lowPrice;

    private String tradingVolume;

    private String fluctuatingRate;


}
