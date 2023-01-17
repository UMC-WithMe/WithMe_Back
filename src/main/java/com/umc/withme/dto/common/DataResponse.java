package com.umc.withme.dto.common;

import lombok.Getter;

@Getter
public class DataResponse<T> extends BaseResponse{

    private T data;

    public DataResponse(T data) {
        super(true);
        this.data = data;
    }
}
