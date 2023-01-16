package com.umc.withme.dto.common;

import com.umc.withme.dto.common.BaseResponse;
import lombok.Getter;

@Getter
public class ErrorResponse extends BaseResponse {

    private int errorCode;

    private String errorMessage;

    public ErrorResponse(int errorCode, String errorMessage) {
        super(false);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
