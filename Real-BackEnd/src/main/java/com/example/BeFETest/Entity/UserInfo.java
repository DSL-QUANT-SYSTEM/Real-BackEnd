package com.example.BeFETest.Entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfo {


    private String name;
    private String phone;
    private String birthDate;
    private String gender;
    private String[] backtestingRecords;
}
