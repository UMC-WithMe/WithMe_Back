package com.umc.withme.dto.meet;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MeetDeleteResponse {

    @Schema(example = "1", description = "삭제 성공한 모임의 id")
    private Long meetId;

    public static MeetDeleteResponse of(Long meetId) {
        return new MeetDeleteResponse(meetId);
    }
}
