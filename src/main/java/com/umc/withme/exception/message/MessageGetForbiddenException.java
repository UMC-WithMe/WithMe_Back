package com.umc.withme.exception.message;

import com.umc.withme.exception.common.ForbiddenException;

public class MessageGetForbiddenException extends ForbiddenException {

    /**
     * 쪽지 채팅방의 멤버가 아닌 사용자가 채팅방 조회를 시도했을 때 발생하는 예외이다.
     * 채팅방 id 와 사용자 id를 입력받아 에러 로그와 함께 출력한다.
     *
     * @param chatroomId 조회하려는 쪽지 채팅방 id
     * @param memberId   현재 로그인한 사용자 id
     */
    public MessageGetForbiddenException(Long chatroomId, Long memberId) {
        super("chatroomId=" + chatroomId + " memberId=" + memberId);
    }
}
