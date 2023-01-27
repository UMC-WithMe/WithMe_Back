package com.umc.withme.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class NicknameDuplicationCheckResponse {

    @Schema(example = "true", description = "닉네임 중복 여부")
    private boolean isDuplicated;

    public static NicknameDuplicationCheckResponse of(boolean isDuplicated) {
        return new NicknameDuplicationCheckResponse(isDuplicated);
    }
}
