package com.example.BeFETest.Entity.Bitcoin;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BitcoinResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @DateTimeFormat(pattern = "yyyy- MM- dd")
    private LocalDate date;

    private Double closingPrice;

    private Double openingPrice;

    private Double highPrice;

    private Double lowPrice;

    private String tradingVolume;

    private String fluctuatingRate;
}
