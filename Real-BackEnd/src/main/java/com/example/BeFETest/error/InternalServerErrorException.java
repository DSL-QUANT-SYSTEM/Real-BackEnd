package com.example.BeFETest.error;

public class InternalServerErrorException extends RuntimeException{

    public InternalServerErrorException(String message){
        super(message);
    }

    public InternalServerErrorException(String message, Throwable cause){
        super(message, cause);
    }
}
