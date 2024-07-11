package com.example.BeFETest.Entity.ubmi30;

import com.example.BeFETest.Entity.ubmi.UbmiResponse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ubmi30_data")
public class Ubmi30Entity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "response_id", nullable = false)
    private Ubmi30Response response;

    @Column(nullable = false)
    private String time;

    @Column(nullable = false)
    private double value;

}
