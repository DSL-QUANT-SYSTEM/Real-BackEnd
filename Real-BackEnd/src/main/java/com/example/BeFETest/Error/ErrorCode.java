package com.example.BeFETest.Error;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public enum ErrorCode {

    BAD_REQUEST(400, "Bad Request!!"),
    UNAUTHORIZED(401, "Unauthorized!!"),
    FORBIDDEN(403, "Access Denied!!"),
    NOT_FOUND(404, "Resource Not Found!!"),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed!!"),
    CONFLICT(409, "Conflict!!"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error!!"),
    BAD_GATEWAY(502, "Bad Gateway!!"),
    SERVICE_UNAVAILABLE(503, "Service Unavailable!!"),
    GATEWAY_TIMEOUT(504, "Gateway Timeout!!");

    private final int status;
    private final String message;

    ErrorCode(int status, String message){
            this.status = status;
            this.message = message;
    }

    public int getStatus() {
            return status;
    }

    public String getMessage(){
            return message;
    }
}
    

/*
@RequiredArgsConstructor
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode implements EnumModel{

    INVALID_CODE(400, "C001", "Invalid Code"),
    RESOURCE_NOT_FOUND(204, "C002", "Resource not found"),
    EXPIRED_CODE(400, "C003", "Expired Code"),
    INTERNAL_ERROR(500, "C004", "Internal Error");;

    private int status;
    private String code;
    private String message;
    private String detail;

    ErrorCode(int status, String code, String message){
        this.status = status;
        this.code = code;
        this.message = message;
    }

    @Override
    public String getKey(){
        return this.code;
    }

    @Override
    public String getValue(){
        return this.message;
    }
}
*/
