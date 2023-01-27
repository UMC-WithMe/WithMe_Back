package com.umc.withme.dto.meet;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MeetCreateResponse {

    @Schema(example = "2", description = "생성 성공한 모임 모집글의 id")
    private Long meetId;

    public static MeetCreateResponse of(Long meetId) {
        return new MeetCreateResponse(meetId);
    }
}
