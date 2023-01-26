package com.umc.withme.exception.meet;

import com.umc.withme.exception.common.BadRequestException;

public class DeleteBadRequestException extends BadRequestException {

    /**
     * 모임의 id와 멤버 이름을 입력받아 에러 로그와 함께 출력한다.
     * @param meetId 삭제하려는 모임의 id
     * @param memberName 삭제하려는 사용자
     */
    public DeleteBadRequestException(Long meetId, String memberName){ super("meetId=" + meetId+" memberName=" + memberName); }
}
