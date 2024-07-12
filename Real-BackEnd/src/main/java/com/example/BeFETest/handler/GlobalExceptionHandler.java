package com.example.BeFETest.handler;


import com.example.BeFETest.Error.CustomExceptions;
import com.example.BeFETest.Error.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

/*
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Bad Request!!", "BAD_REQUEST");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpClientErrorException.Unauthorized.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(HttpClientErrorException.Unauthorized ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), "Unauthorized!!", "UNAUTHORIZED");
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(HttpClientErrorException.Forbidden.class)
    public ResponseEntity<ErrorResponse> handleForbiddenException(HttpClientErrorException.Forbidden ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.FORBIDDEN.value(), "Forbidden!!", "FORBIDDEN");
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ConfigDataResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ConfigDataResourceNotFoundException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), "Resource Not Found!!", "NOT_FOUND");
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodNotAllowedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotAllowedException(MethodNotAllowedException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.METHOD_NOT_ALLOWED.value(), "Method Not Allowed!!", "METHOD_NOT_ALLOWED");
        return new ResponseEntity<>(errorResponse, HttpStatus.METHOD_NOT_ALLOWED);
    }


    @ExceptionHandler(HttpClientErrorException.Conflict.class)
    public ResponseEntity<ErrorResponse> handleConflictException(HttpClientErrorException.Conflict ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.CONFLICT.value(), "Conflict!!", "CONFLICT");
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(HttpServerErrorException.InternalServerError.class)
    public ResponseEntity<ErrorResponse> handleInternalServerErrorException(HttpServerErrorException.InternalServerError ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error!!", "INTERNAL_SERVER_ERROR");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HttpServerErrorException.BadGateway.class)
    public ResponseEntity<ErrorResponse> handleBadGatewayException(HttpServerErrorException.BadGateway ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_GATEWAY.value(), "Bad Gateway!!", "BAD_GATEWAY");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<ErrorResponse> handleServiceUnavailableException(ServiceUnavailableException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.SERVICE_UNAVAILABLE.value(), "Service Unavailable!!", "SERVICE_UNAVAILABLE");
        return new ResponseEntity<>(errorResponse, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(HttpServerErrorException.GatewayTimeout.class)
    public ResponseEntity<ErrorResponse> handleGatewayTimeoutException(HttpServerErrorException.GatewayTimeout ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.GATEWAY_TIMEOUT.value(), "Gateway Timeout!!", "GATEWAY_TIMEOUT");
        return new ResponseEntity<>(errorResponse, HttpStatus.GATEWAY_TIMEOUT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error!!", "INTERNAL_SERVER_ERROR");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
*/


/*
@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> globalExceptionHandler(Exception ex, WebRequest request) {
        return new ResponseEntity<>(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error!!"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

      @ExceptionHandler(CustomExceptions.BadRequestException.class)
      public ResponseEntity<String> handleBadRequest(CustomExceptions.BadRequestException ex) {
          return ResponseEntity.status(ex.getErrorCode().getStatus()).body(ex.getErrorCode().getMessage());
      }


      //@ExceptionHandler(CustomExceptions.UnauthorizedException.class)
      //public ResponseEntity<String> handleUnauthorized(CustomExceptions.UnauthorizedException ex) {
      //    return ResponseEntity.status(ex.getErrorCode().getStatus()).body(ex.getErrorCode().getMessage());
      //}


      @ExceptionHandler(CustomExceptions.UnauthorizedException.class)
      public ResponseEntity<ErrorResponse> handleUnauthorized(CustomExceptions.UnauthorizedException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getErrorCode().getStatus(), ex.getErrorCode().getMessage());
        return ResponseEntity.status(ex.getErrorCode().getStatus()).body(errorResponse);
      }

      @ExceptionHandler(CustomExceptions.ForbiddenException.class)
      public ResponseEntity<ErrorResponse> handleForbidden(CustomExceptions.ForbiddenException ex) {
          ErrorResponse errorResponse = new ErrorResponse(ex.getErrorCode().getStatus(), ex.getErrorCode().getMessage());
          return ResponseEntity.status(ex.getErrorCode().getStatus()).body(errorResponse);
      }

      @ExceptionHandler(CustomExceptions.ResourceNotFoundException.class)
      public ResponseEntity<String> handleNotFound(CustomExceptions.ResourceNotFoundException ex) {
          return ResponseEntity.status(ex.getErrorCode().getStatus()).body(ex.getErrorCode().getMessage());
      }

      @ExceptionHandler(CustomExceptions.MethodNotAllowedException.class)
      public ResponseEntity<String> handleMethodNotAllowed(CustomExceptions.MethodNotAllowedException ex) {
          return ResponseEntity.status(ex.getErrorCode().getStatus()).body(ex.getErrorCode().getMessage());
      }

      @ExceptionHandler(CustomExceptions.ConflictException.class)
      public ResponseEntity<String> handleConflict(CustomExceptions.ConflictException ex) {
          return ResponseEntity.status(ex.getErrorCode().getStatus()).body(ex.getErrorCode().getMessage());
      }

      @ExceptionHandler(CustomExceptions.InternalServerErrorException.class)
      public ResponseEntity<String> handleInternalServerError(CustomExceptions.InternalServerErrorException ex) {
          return ResponseEntity.status(ex.getErrorCode().getStatus()).body(ex.getErrorCode().getMessage());
      }

      @ExceptionHandler(CustomExceptions.BadGatewayException.class)
      public ResponseEntity<String> handleBadGateway(CustomExceptions.BadGatewayException ex) {
          return ResponseEntity.status(ex.getErrorCode().getStatus()).body(ex.getErrorCode().getMessage());
      }

      @ExceptionHandler(CustomExceptions.ServiceUnavailableException.class)
      public ResponseEntity<String> handleServiceUnavailable(CustomExceptions.ServiceUnavailableException ex) {
          return ResponseEntity.status(ex.getErrorCode().getStatus()).body(ex.getErrorCode().getMessage());
      }

      @ExceptionHandler(CustomExceptions.GatewayTimeoutException.class)
      public ResponseEntity<String> handleGatewayTimeout(CustomExceptions.GatewayTimeoutException ex) {
          return ResponseEntity.status(ex.getErrorCode().getStatus()).body(ex.getErrorCode().getMessage());
      }
}
*/
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> globalExceptionHandler(Exception ex, WebRequest request) {
        String requestUrl = request.getDescription(false).substring(4);  // URL 정보
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error!!",
                String.format("Exception occurred at %s: %s", requestUrl, ex.getMessage())
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CustomExceptions.BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(CustomExceptions.BadRequestException ex, WebRequest request) {
        String requestUrl = request.getDescription(false).substring(4);
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getErrorCode().getStatus(),
                ex.getErrorCode().getMessage(),
                String.format("Exception occurred at %s: %s", requestUrl, ex.getDetailedMessage())
        );
        return ResponseEntity.status(ex.getErrorCode().getStatus()).body(errorResponse);
    }

    @ExceptionHandler(CustomExceptions.UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorized(CustomExceptions.UnauthorizedException ex, WebRequest request) {
        String requestUrl = request.getDescription(false).substring(4);
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getErrorCode().getStatus(),
                ex.getErrorCode().getMessage(),
                String.format("Exception occurred at %s: %s", requestUrl, ex.getDetailedMessage())
        );
        return ResponseEntity.status(ex.getErrorCode().getStatus()).body(errorResponse);
    }

    @ExceptionHandler(CustomExceptions.ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleForbidden(CustomExceptions.ForbiddenException ex, WebRequest request) {
        String requestUrl = request.getDescription(false).substring(4);
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getErrorCode().getStatus(),
                ex.getErrorCode().getMessage(),
                String.format("Exception occurred at %s: %s", requestUrl, ex.getDetailedMessage())
        );
        return ResponseEntity.status(ex.getErrorCode().getStatus()).body(errorResponse);
    }

    @ExceptionHandler(CustomExceptions.ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(CustomExceptions.ResourceNotFoundException ex, WebRequest request) {
        String requestUrl = request.getDescription(false).substring(4);
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getErrorCode().getStatus(),
                ex.getErrorCode().getMessage(),
                String.format("Exception occurred at %s: %s", requestUrl, ex.getDetailedMessage())
        );
        return ResponseEntity.status(ex.getErrorCode().getStatus()).body(errorResponse);
    }

    @ExceptionHandler(CustomExceptions.MethodNotAllowedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotAllowed(CustomExceptions.MethodNotAllowedException ex, WebRequest request) {
        String requestUrl = request.getDescription(false).substring(4);
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getErrorCode().getStatus(),
                ex.getErrorCode().getMessage(),
                String.format("Exception occurred at %s: %s", requestUrl, ex.getDetailedMessage())
        );
        return ResponseEntity.status(ex.getErrorCode().getStatus()).body(errorResponse);
    }

    @ExceptionHandler(CustomExceptions.ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflict(CustomExceptions.ConflictException ex, WebRequest request) {
        String requestUrl = request.getDescription(false).substring(4);
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getErrorCode().getStatus(),
                ex.getErrorCode().getMessage(),
                String.format("Exception occurred at %s: %s", requestUrl, ex.getDetailedMessage())
        );
        return ResponseEntity.status(ex.getErrorCode().getStatus()).body(errorResponse);
    }

    @ExceptionHandler(CustomExceptions.InternalServerErrorException.class)
    public ResponseEntity<ErrorResponse> handleInternalServerError(CustomExceptions.InternalServerErrorException ex, WebRequest request) {
        String requestUrl = request.getDescription(false).substring(4);
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getErrorCode().getStatus(),
                ex.getErrorCode().getMessage(),
                String.format("Exception occurred at %s: %s", requestUrl, ex.getDetailedMessage())
        );
        return ResponseEntity.status(ex.getErrorCode().getStatus()).body(errorResponse);
    }

    @ExceptionHandler(CustomExceptions.BadGatewayException.class)
    public ResponseEntity<ErrorResponse> handleBadGateway(CustomExceptions.BadGatewayException ex, WebRequest request) {
        String requestUrl = request.getDescription(false).substring(4);
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getErrorCode().getStatus(),
                ex.getErrorCode().getMessage(),
                String.format("Exception occurred at %s: %s", requestUrl, ex.getDetailedMessage())
        );
        return ResponseEntity.status(ex.getErrorCode().getStatus()).body(errorResponse);
    }

    @ExceptionHandler(CustomExceptions.ServiceUnavailableException.class)
    public ResponseEntity<ErrorResponse> handleServiceUnavailable(CustomExceptions.ServiceUnavailableException ex, WebRequest request) {
        String requestUrl = request.getDescription(false).substring(4);
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getErrorCode().getStatus(),
                ex.getErrorCode().getMessage(),
                String.format("Exception occurred at %s: %s", requestUrl, ex.getDetailedMessage())
        );
        return ResponseEntity.status(ex.getErrorCode().getStatus()).body(errorResponse);
    }

    @ExceptionHandler(CustomExceptions.GatewayTimeoutException.class)
    public ResponseEntity<ErrorResponse> handleGatewayTimeout(CustomExceptions.GatewayTimeoutException ex, WebRequest request) {
        String requestUrl = request.getDescription(false).substring(4);
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getErrorCode().getStatus(),
                ex.getErrorCode().getMessage(),
                String.format("Exception occurred at %s: %s", requestUrl, ex.getDetailedMessage())
        );
        return ResponseEntity.status(ex.getErrorCode().getStatus()).body(errorResponse);
    }
}

 
