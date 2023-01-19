package com.umc.withme.exception.common;

import org.springframework.http.HttpStatus;

public class BadRequestException extends CustomException {

    public BadRequestException() {
        super(HttpStatus.BAD_REQUEST);
    }

    public BadRequestException(String optionalMessage) {
        super(HttpStatus.BAD_REQUEST, optionalMessage);
    }
}
