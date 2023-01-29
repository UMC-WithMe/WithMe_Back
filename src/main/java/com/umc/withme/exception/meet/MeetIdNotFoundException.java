package com.umc.withme.exception.meet;

import com.umc.withme.exception.common.NotFoundException;

public class MeetIdNotFoundException extends NotFoundException {

    /**
     * 모임의 id를 입력받아 에러 로그와 함께 출력한다.
     *
     * @param meetId 조회시도 한 모임의 id
     */
    public MeetIdNotFoundException(Long meetId) {
        super("meetId=" + meetId);
    }
}
