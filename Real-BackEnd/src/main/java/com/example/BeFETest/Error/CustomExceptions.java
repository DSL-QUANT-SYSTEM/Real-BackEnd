package com.example.BeFETest.Error;

public class CustomExceptions {


    public static class CustomException extends RuntimeException {
        private final ErrorCode errorCode;

        public CustomException(ErrorCode errorCode) {
            super(errorCode.getMessage());
            this.errorCode = errorCode;
        }

        public ErrorCode getErrorCode() {
            return errorCode;
        }
    }

    public static class BadRequestException extends CustomException {
        public BadRequestException() {
            super(ErrorCode.BAD_REQUEST);
        }
    }

    public static class UnauthorizedException extends CustomException {
        public UnauthorizedException() {
            super(ErrorCode.UNAUTHORIZED);
        }
    }

    public static class ForbiddenException extends CustomException {
        public ForbiddenException() {
            super(ErrorCode.FORBIDDEN);
        }
    }

    public static class ResourceNotFoundException extends CustomException {
        public ResourceNotFoundException() {
            super(ErrorCode.NOT_FOUND);
        }
    }

    public static class MethodNotAllowedException extends CustomException {
        public MethodNotAllowedException() {
            super(ErrorCode.METHOD_NOT_ALLOWED);
        }
    }

    public static class ConflictException extends CustomException {
        public ConflictException() {
            super(ErrorCode.CONFLICT);
        }
    }

    public static class InternalServerErrorException extends CustomException {
        public InternalServerErrorException() {
            super(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    public static class BadGatewayException extends CustomException {
        public BadGatewayException() {
            super(ErrorCode.BAD_GATEWAY);
        }
    }

    public static class ServiceUnavailableException extends CustomException {
        public ServiceUnavailableException() {
            super(ErrorCode.SERVICE_UNAVAILABLE);
        }
    }

    public static class GatewayTimeoutException extends CustomException {
        public GatewayTimeoutException() {
            super(ErrorCode.GATEWAY_TIMEOUT);
        }
    }
}



/*
public class CustomExceptions {

  public static class BadRequestException extends CustomException{
      public BadRequestException() {
          super(ErrorCode.BAD_REQUEST);
      }
  }

  public static class UnauthorizedException extends CustomException {
      public UnauthorizedException() {
          super(ErrorCode.UNAUTHORIZED);
      }
  }

  public static class ForbiddenException extends CustomException {
      public ForbiddenException() {
          super(ErrorCode.FORBIDDEN);
      }
  }

  public static class ResourceNotFoundException extends CustomException {
      public ResourceNotFoundException() {  
          super(ErrorCode.NOT_FOUND);
      }
  }

  public static class MethodNotAllowedException extends CustomException {
      public MethodNotAllowedException() {
          super(ErrorCode.METHOD_NOT_ALLOWED);
      }
  }

  public static class ConflictException extends CustomException {
      public ConflictException() {
          super(ErrorCode.CONFLICT);
      }
  }

  public static class InternalServerErrorException extends CustomException {
      public InternalServerErrorException() {
          super(ErrorCode.INTERNAL_SERVER_ERROR);
      }
  }

  public static class BadGatewayException extends CustomException {
      public BadGatewayException() {
          super(ErrorCode.BAD_GATEWAY);
      }
  }

  public static class ServiceUnavailableException extends CustomException {
      public ServiceUnavailableException() {
          super(ErrorCode.SERVICE_UNAVAILABLE);
      }
  }

  public static class GatewayTimeoutException extends CustomException {
        public GatewayTimeoutException() {
            super(ErrorCode.GATEWAY_TIMEOUT);
        }
  }

  public static abstract class CustomException extends RuntimeException {
        private final ErrorCode errorCode;

        public CustomException(ErrorCode errorCode) {
            super(errorCode.getMessage());
            this.errorCode = errorCode;
        }

        public ErrorCode getErrorCode() {
            return errorCode;
        }
  }

        
      
  
        
      
    
}

 */
