package com.umc.withme.exception.common;

import org.springframework.http.HttpStatus;

public class NotFoundException extends CustomException {

    public NotFoundException() {
        super(HttpStatus.NOT_FOUND);
    }

    public NotFoundException(String optionalMessage) {
        super(HttpStatus.NOT_FOUND, optionalMessage);
    }
}
