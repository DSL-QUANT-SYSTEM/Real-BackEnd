// KospiEntity.java
package com.example.BeFETest.entity.kospi;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "kospi_data")
public class KospiEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "response_id", nullable = false)
    private KospiResponse response;

    @Column(nullable = false)
    private String time;

    @Column(nullable = false)
    private double value;

}
