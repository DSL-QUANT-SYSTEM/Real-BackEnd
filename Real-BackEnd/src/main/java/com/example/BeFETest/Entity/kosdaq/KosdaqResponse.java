package com.example.BeFETest.Entity.kosdaq;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class KosdaqResponse {

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