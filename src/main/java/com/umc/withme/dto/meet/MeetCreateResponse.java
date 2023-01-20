package com.umc.withme.dto.meet;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MeetCreateResponse {

   private Long meetId;

    public static MeetCreateResponse from(MeetDto dto){
        return new MeetCreateResponse(dto.getMeetId());
    }
}
