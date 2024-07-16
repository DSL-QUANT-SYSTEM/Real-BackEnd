package com.example.BeFETest.Error;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> globalExceptionHandler(Exception ex, WebRequest request) {
        String requestUrl = request.getDescription(false).substring(4);
        String methodName = getMethodNameFromException(ex);

        String detailedMessage = ex instanceof CustomExceptions.InternalServerErrorException ?
                ((CustomExceptions.InternalServerErrorException) ex).getDetailedMessage() :
                ex.getMessage();
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error!!",
                String.format("Exception occurred at %s: %s. 에러가 발생한 함수: %s", requestUrl, detailedMessage, methodName)
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(CustomExceptions.BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(CustomExceptions.BadRequestException ex, WebRequest request) {
        String requestUrl = request.getDescription(false).substring(4);
        String methodName = getMethodNameFromException(ex);
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getErrorCode().getStatus(),
                ex.getErrorCode().getMessage(),
                String.format("Exception occurred at %s: %s. 에러가 발생한 함수: %s", requestUrl, ex.getDetailedMessage(), methodName)
        );
        return ResponseEntity.status(ex.getErrorCode().getStatus()).body(errorResponse);
    }

    @ExceptionHandler(CustomExceptions.UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorized(CustomExceptions.UnauthorizedException ex, WebRequest request) {
        String requestUrl = request.getDescription(false).substring(4);
        String methodName = getMethodNameFromException(ex);
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getErrorCode().getStatus(),
                ex.getErrorCode().getMessage(),
                String.format("Exception occurred at %s: %s. 에러가 발생한 함수: %s", requestUrl, ex.getDetailedMessage(), methodName)
        );
        return ResponseEntity.status(ex.getErrorCode().getStatus()).body(errorResponse);
    }

    @ExceptionHandler(CustomExceptions.ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleForbidden(CustomExceptions.ForbiddenException ex, WebRequest request) {
        String requestUrl = request.getDescription(false).substring(4);
        String methodName = getMethodNameFromException(ex);
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getErrorCode().getStatus(),
                ex.getErrorCode().getMessage(),
                String.format("Exception occurred at %s: %s. 에러가 발생한 함수: %s", requestUrl, ex.getDetailedMessage(), methodName)
        );
        return ResponseEntity.status(ex.getErrorCode().getStatus()).body(errorResponse);
    }

    @ExceptionHandler(CustomExceptions.ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(CustomExceptions.ResourceNotFoundException ex, WebRequest request) {
        String requestUrl = request.getDescription(false).substring(4);
        String methodName = getMethodNameFromException(ex);
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getErrorCode().getStatus(),
                ex.getErrorCode().getMessage(),
                String.format("Exception occurred at %s: %s. 에러가 발생한 함수: %s", requestUrl, ex.getDetailedMessage(), methodName)
        );
        return ResponseEntity.status(ex.getErrorCode().getStatus()).body(errorResponse);
    }

    @ExceptionHandler(CustomExceptions.MethodNotAllowedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotAllowed(CustomExceptions.MethodNotAllowedException ex, WebRequest request) {
        String requestUrl = request.getDescription(false).substring(4);
        String methodName = getMethodNameFromException(ex);
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getErrorCode().getStatus(),
                ex.getErrorCode().getMessage(),
                String.format("Exception occurred at %s: %s. 에러가 발생한 함수: %s", requestUrl, ex.getDetailedMessage(), methodName)
        );
        return ResponseEntity.status(ex.getErrorCode().getStatus()).body(errorResponse);
    }

    @ExceptionHandler(CustomExceptions.ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflict(CustomExceptions.ConflictException ex, WebRequest request) {
        String requestUrl = request.getDescription(false).substring(4);
        String methodName = getMethodNameFromException(ex);
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getErrorCode().getStatus(),
                ex.getErrorCode().getMessage(),
                String.format("Exception occurred at %s: %s. 에러가 발생한 함수: %s", requestUrl, ex.getDetailedMessage(), methodName)
        );
        return ResponseEntity.status(ex.getErrorCode().getStatus()).body(errorResponse);
    }

    @ExceptionHandler(CustomExceptions.BadGatewayException.class)
    public ResponseEntity<ErrorResponse> handleBadGateway(CustomExceptions.BadGatewayException ex, WebRequest request) {
        String requestUrl = request.getDescription(false).substring(4);
        String methodName = getMethodNameFromException(ex);
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getErrorCode().getStatus(),
                ex.getErrorCode().getMessage(),
                String.format("Exception occurred at %s: %s. 에러가 발생한 함수: %s", requestUrl, ex.getDetailedMessage(), methodName)
        );
        return ResponseEntity.status(ex.getErrorCode().getStatus()).body(errorResponse);
    }

    @ExceptionHandler(CustomExceptions.ServiceUnavailableException.class)
    public ResponseEntity<ErrorResponse> handleServiceUnavailable(CustomExceptions.ServiceUnavailableException ex, WebRequest request) {
        String requestUrl = request.getDescription(false).substring(4);
        String methodName = getMethodNameFromException(ex);
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getErrorCode().getStatus(),
                ex.getErrorCode().getMessage(),
                String.format("Exception occurred at %s: %s. 에러가 발생한 함수: %s", requestUrl, ex.getDetailedMessage(), methodName)
        );
        return ResponseEntity.status(ex.getErrorCode().getStatus()).body(errorResponse);
    }

    @ExceptionHandler(CustomExceptions.GatewayTimeoutException.class)
    public ResponseEntity<ErrorResponse> handleGatewayTimeout(CustomExceptions.GatewayTimeoutException ex, WebRequest request) {
        String requestUrl = request.getDescription(false).substring(4);
        String methodName = getMethodNameFromException(ex);
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getErrorCode().getStatus(),
                ex.getErrorCode().getMessage(),
                String.format("Exception occurred at %s: %s. 에러가 발생한 함수: %s", requestUrl, ex.getDetailedMessage(), methodName)
        );
        return ResponseEntity.status(ex.getErrorCode().getStatus()).body(errorResponse);
    }

    // 공통 메서드로 예외 발생 함수명 추출
    private String getMethodNameFromException(Exception ex) {
        StackTraceElement[] stackTrace = ex.getStackTrace();
        if (stackTrace.length > 0) {
            return stackTrace[0].getMethodName();
        }
        return "알 수 없음";
    }
}
