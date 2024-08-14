package com.example.BeFETest.Entity.kospi;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
@Builder
@Getter
@Setter
@Entity
@Table(name = "kospi_response")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class KospiResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String date;

    private String closingPrice;

    private String openingPrice;

    private String highPrice;

    private String lowPrice;

    private String tradingVolume;

    private String fluctuatingRate;





}
