package com.umc.withme.dto.meet;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MeetDeleteResponse {

    private Long meetId;

    public static MeetDeleteResponse of(Long meetId) {
        return new MeetDeleteResponse(meetId);
    }
}
