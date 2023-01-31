package com.umc.withme.exception.meet;

import com.umc.withme.exception.common.ForbiddenException;

public class MeetUpdateForbiddenException extends ForbiddenException {

    /**
     * 모임의 리더가 아닌 사용자가 모임을 수정하려고 시도했을 때 발생하는 예외이다.
     * 모임의 id와 멤버의 id를 입력받아 에러 로그와 함께 출력한다.
     *
     * @param meetId   수정하려는 모임의 id
     * @param memberId 수정하려는 사용자의 id
     */
    public MeetUpdateForbiddenException(Long meetId, Long memberId) {
        super("meetId=" + meetId + " memberId=" + memberId);
    }
}
