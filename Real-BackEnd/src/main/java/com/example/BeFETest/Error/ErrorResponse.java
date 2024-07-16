package com.example.BeFETest.Error;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {


    private int status;
    private String message;
    private String detailedMessage;


    public ErrorResponse(int status, String message, String detailedMessage) {
        this.status = status;
        this.message = message;
        this.detailedMessage = detailedMessage;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getDetailedMessage(){
        return detailedMessage;
    }

}


