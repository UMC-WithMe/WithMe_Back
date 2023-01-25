package com.umc.withme.dto.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BaseResponse {

    @Schema(example = "true", description = "요청 처리 성공 시 true, 실패 시 false")
    private boolean isSuccess;
}
