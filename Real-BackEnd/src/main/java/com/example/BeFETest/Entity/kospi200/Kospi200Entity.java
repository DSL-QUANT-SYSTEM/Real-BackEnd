package com.example.BeFETest.Entity.kospi200;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/*
@Getter
@Setter
@Entity
@Table(name = "kospi200_data")
public class Kospi200Entity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "response_id", nullable = false)
    private Kospi200Response response;

    @Column(nullable = false)
    private String time;

    @Column(nullable = false)
    private double value;

}
*/