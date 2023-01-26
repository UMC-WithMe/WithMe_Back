package com.umc.withme.exception.meet;

import com.umc.withme.exception.common.NotFoundException;

public class MeetIdNotFoundException extends NotFoundException {

    /**
     * 모임의 Id를 입력받아 에러 로그와 함께 출력한다.
     * @param meetId
     */
    public MeetIdNotFoundException(Long meetId){ super("meetId=" + meetId); }
}
