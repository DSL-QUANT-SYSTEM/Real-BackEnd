package com.example.BeFETest.handler;

@RestControllerAdvice
public class GlobalExceptionHandler {

      @ExceptionHandler(CustomExceptions.BadRequestException.class)
      public ResponseEntity<String> handleBadRequest(CustomExceptions.BadRequestException ex) {
          return ResponseEntity.status(ex.getErrorCode().getStatus()).body(ex.getErrorCode().getMessage());
      }

      @ExceptionHandler(CustomExceptions.UnauthorizedException.class)
      public ResponseEntity<String> handleUnauthorized(CustomExceptions.UnauthorizedException ex) {
          return ResponseEntity.status(ex.getErrorCode().getStatus()).body(ex.getErrorCode().getMessage());
      }

      @ExceptionHandler(CustomExceptions.ForbiddenException.class)
      public ResponseEntity<String> handleForbidden(CustomExceptions.ForbiddenException ex) {
          return ResponseEntity.status(ex.getErrorCode().getStatus()).body(ex.getErrorCode().getMessage());
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
