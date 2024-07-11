package com.example.BeFETest.DTO.ubmi;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UbmiDTO {

    private Long id;
    private String time;
    private double value;

    public UbmiDTO() {}

    public UbmiDTO(Long id, String time, double value) {
        this.id = id;
        this.time = time;
        this.value = value;
    }


}
