package com.example.BeFETest.Entity.kosdak;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "kosdak_data")
public class KosdakEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "response_id", nullable = false)
    private KosdakResponse response;

    @Column(nullable = false)
    private String time;

    @Column(nullable = false)
    private double value;

}
