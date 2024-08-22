package com.example.BeFETest.DTO.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

    public UserDTO(String name, String email){
        this.name = name;
        this.email = email;
    }

    private String name;
    private String email;
}