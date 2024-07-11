package com.example.BeFETest.DTO.kosdak2000;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Kosdak2000DTO {

    private Long id;
    private String time;
    private double value;

    public Kosdak2000DTO() {}

    public Kosdak2000DTO(Long id, String time, double value) {
        this.id = id;
        this.time = time;
        this.value = value;
    }


}
