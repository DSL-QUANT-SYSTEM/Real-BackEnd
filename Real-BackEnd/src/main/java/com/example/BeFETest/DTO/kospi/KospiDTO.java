package com.example.BeFETest.DTO.kospi;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KospiDTO {

    private Long id;
    private String time;
    private double value;

    public KospiDTO() {}

    public KospiDTO(Long id, String time, double value) {
        this.id = id;
        this.time = time;
        this.value = value;
    }


}
