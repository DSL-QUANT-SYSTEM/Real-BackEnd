package com.example.BeFETest.Error;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {
    private int status;
    private String message;
    private String errorCode;

    public ErrorResponse(int status, String message, String errorCode) {
        this.status = status;
        this.message = message;
        this.errorCode = errorCode;
    }

    // Getters and Setters
}

/*
@Getter
@Setter
@NoArgsConstructor
public class ErrorResponse {

    private String message;
    private String code;
    private int status;
    private String detail;

    /*
    public ErrorResponse(ErrorCode code){
        this.message = code.getMessage();
        this.status = code.getStatus();
        this.code = code.getCode();
        this.detail = code.getDetail();
    }

    public static ErrorResponse of(ErrorCode code){
        return new ErrorResponse(code);
    }*/
//}

