package com.umc.withme.exception.member;

import com.umc.withme.exception.common.NotFoundException;

public class MemberIdNotFoundException extends NotFoundException {
    /**
     * 사용자의 id를 입력받아 에러 로그와 함께 출력한다.
     *
     * @param memberId 조회시도 한 사용자의 id
     */
    public MemberIdNotFoundException(Long memberId) {
        super("memberId=" + memberId);
    }
}
