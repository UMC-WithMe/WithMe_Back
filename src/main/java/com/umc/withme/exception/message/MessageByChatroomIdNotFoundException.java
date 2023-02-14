package com.umc.withme.exception.message;

import com.umc.withme.exception.common.NotFoundException;

public class MessageByChatroomIdNotFoundException extends NotFoundException {

    /**
     * 채팅방의 id를 입력받아 에러 로그와 함께 출력한다.
     *
     * @param chatroomId 조회시도 한 채팅방 id
     */
    public MessageByChatroomIdNotFoundException(Long chatroomId) {
        super("chatroomId=" + chatroomId);
    }
}
