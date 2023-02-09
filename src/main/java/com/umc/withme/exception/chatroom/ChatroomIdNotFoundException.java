package com.umc.withme.exception.chatroom;

import com.umc.withme.exception.common.NotFoundException;

public class ChatroomIdNotFoundException extends NotFoundException {
    /**
     * 채팅방 id를 입력받아 에러 로그와 함께 출력한다.
     *
     * @param chatroomId 조회시도 한 채팅방 id
     */
    public ChatroomIdNotFoundException(Long chatroomId) {
        super("chatroomId=" + chatroomId);
    }
}
