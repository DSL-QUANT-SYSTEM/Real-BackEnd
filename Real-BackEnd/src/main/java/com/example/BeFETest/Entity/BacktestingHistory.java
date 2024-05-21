package com.example.BeFETest.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "backtesting_history")
public class BacktestingHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String date;

    @Column(nullable = false)
    private String universe;

    @Column(nullable = false)
    private double weight;

    @Column(name = "initial_investment", nullable = false)
    private double initialInvestment;

    @Column(nullable = false)
    private int period;

    @Column(name = "file_html", nullable = false)
    private String fileHtml;
}
