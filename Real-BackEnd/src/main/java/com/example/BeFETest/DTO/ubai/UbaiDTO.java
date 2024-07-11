package com.example.BeFETest.DTO.ubai;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UbaiDTO {

    private Long id;
    private String time;
    private double value;

    public UbaiDTO() {}

    public UbaiDTO(Long id, String time, double value) {
        this.id = id;
        this.time = time;
        this.value = value;
    }


}
