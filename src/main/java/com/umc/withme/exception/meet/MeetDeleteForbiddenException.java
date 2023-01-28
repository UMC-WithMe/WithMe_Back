package com.umc.withme.exception.meet;

import com.umc.withme.exception.common.ForbiddenException;

public class MeetDeleteForbiddenException extends ForbiddenException {

    /**
     * 모임의 리더가 아닌 사용자가 모임을 삭제하려고 시도했을 때 발생하는 예외이다.
     * 모임의 id와 멤버 이메일을 입력받아 에러 로그와 함께 출력한다.
     *
     * @param meetId      삭제하려는 모임의 id
     * @param memberEmail 삭제하려는 사용자
     */
    public MeetDeleteForbiddenException(Long meetId, String memberEmail) {
        super("meetId=" + meetId + " memberEmail=" + memberEmail);
    }
}
