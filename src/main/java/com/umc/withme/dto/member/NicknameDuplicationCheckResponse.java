package com.umc.withme.dto.member;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class NicknameDuplicationCheckResponse {

    private boolean isDuplicated;

    public static NicknameDuplicationCheckResponse of(boolean isDuplicated) {
        return new NicknameDuplicationCheckResponse(isDuplicated);
    }
}
