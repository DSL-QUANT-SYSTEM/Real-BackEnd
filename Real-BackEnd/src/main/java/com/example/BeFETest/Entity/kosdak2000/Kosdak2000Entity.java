package com.example.BeFETest.Entity.kosdak2000;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "kosdak2000_data")
public class Kosdak2000Entity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "response_id", nullable = false)
    private Kosdak2000Response response;

    @Column(nullable = false)
    private String time;

    @Column(nullable = false)
    private double value;

}
