package com.umc.withme.exception.meet_like;

import com.umc.withme.exception.common.ConflictException;

public class MeetLikeConflictException extends ConflictException {
    public MeetLikeConflictException(Long memberId, Long meetId){
        super("memberId: "+memberId+", meetId: "+meetId);
    };
}
