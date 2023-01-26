package com.umc.withme.dto.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class ErrorResponse extends BaseResponse {

    @Schema(example = "0", description = "UMC Server 팀에서 정한 내부적인 error code")
    private int errorCode;

    @Schema(example = "Error message", description = "에러 상황에 대한 설명")
    private String errorMessage;

    public ErrorResponse(int errorCode, String errorMessage) {
        super(false);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
