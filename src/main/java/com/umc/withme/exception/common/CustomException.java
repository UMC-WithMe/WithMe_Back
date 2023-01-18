package com.umc.withme.exception.common;

import com.umc.withme.exception.ExceptionType;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final int errorCode;
    private final String errorMessage;

    public CustomException(HttpStatus httpStatus) {
        ExceptionType exceptionType = ExceptionType.from(this.getClass());
        this.httpStatus = httpStatus;
        this.errorCode = exceptionType.getErrorCode();
        this.errorMessage = exceptionType.getErrorMessage();
    }

    public CustomException(HttpStatus httpStatus, String optionalMessage) {
        ExceptionType exceptionType = ExceptionType.from(this.getClass());
        this.httpStatus = httpStatus;
        this.errorCode = exceptionType.getErrorCode();
        this.errorMessage = exceptionType.getErrorMessage() + " " + optionalMessage;
    }
}
