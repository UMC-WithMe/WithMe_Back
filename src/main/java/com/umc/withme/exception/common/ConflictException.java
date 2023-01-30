package com.umc.withme.exception.common;

import org.springframework.http.HttpStatus;

public class ConflictException extends CustomException {

    public ConflictException() {
        super(HttpStatus.CONFLICT);
    }

    public ConflictException(String optionalMessage) {
        super(HttpStatus.CONFLICT, optionalMessage);
    }
}
