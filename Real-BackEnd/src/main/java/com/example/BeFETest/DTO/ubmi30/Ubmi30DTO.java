package com.example.BeFETest.DTO.ubmi30;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Ubmi30DTO {

    private Long id;
    private String time;
    private double value;

    public Ubmi30DTO() {}

    public Ubmi30DTO(Long id, String time, double value) {
        this.id = id;
        this.time = time;
        this.value = value;
    }


}
