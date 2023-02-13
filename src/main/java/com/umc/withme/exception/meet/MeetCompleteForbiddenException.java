package com.umc.withme.exception.meet;

import com.umc.withme.exception.common.ForbiddenException;

public class MeetCompleteForbiddenException extends ForbiddenException {

    /**
     * 모임의 리더가 아닌 사용자가 모임을 해제하려고 했을 때 발생하는 예외이다.
     * 모임의 id와 사용자 id를 입력받아 에러 로그와 함께 출력한다.
     *
     * @param meetId 해제하려는 모임 id
     * @param memberId 해제하려는 사용자 id
     */
    public MeetCompleteForbiddenException(Long meetId, Long memberId){
        super("meetId=" + meetId + " memberId=" + memberId);
    }
}
