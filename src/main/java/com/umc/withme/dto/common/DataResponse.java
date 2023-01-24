package com.umc.withme.dto.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class DataResponse<T> extends BaseResponse{

    @Schema(description = "응답 데이터")
    private T data;

    public DataResponse(T data) {
        super(true);
        this.data = data;
    }
}
