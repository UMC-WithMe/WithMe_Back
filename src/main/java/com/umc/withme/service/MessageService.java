package com.umc.withme.service;

import com.umc.withme.domain.Chatroom;
import com.umc.withme.domain.Meet;
import com.umc.withme.domain.Member;
import com.umc.withme.domain.Message;
import com.umc.withme.dto.message.MessageCreateRequest;
import com.umc.withme.dto.message.MessageDto;
import com.umc.withme.exception.chatroom.ChatroomIdNotFoundException;
import com.umc.withme.exception.meet.MeetIdNotFoundException;
import com.umc.withme.exception.member.MemberIdNotFoundException;
import com.umc.withme.exception.message.MessageGetForbiddenException;
import com.umc.withme.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MessageService {

    private final MeetRepository meetRepository;
    private final MeetAddressRepository meetAddressRepository;
    private final AddressRepository addressRepository;
    private final MessageRepository messageRepository;
    private final MemberRepository memberRepository;
    private final ChatroomRepository chatroomRepository;

    /**
     * 새로운 쪽지 생성하여 DB에 저장
     *
     * @param senderId             쪽지 보낸 회원 id(pk)
     * @param receiverId           쪽지 받는 회원 id(pk)
     * @param meetId               관련 모집글 id(pk)
     * @param messageCreateRequest 쪽지 생성 요청 데이터 (쪽지 내용)
     * @return 생성된(DB에 저장된) 쪽지 id(pk)
     */
    @Transactional
    public Long createMessage(Long senderId, Long receiverId, Long meetId, MessageCreateRequest messageCreateRequest) {

        Member sender = memberRepository.findById(senderId).orElseThrow(() -> new MemberIdNotFoundException(senderId));
        Member receiver = memberRepository.findById(receiverId).orElseThrow(() -> new MemberIdNotFoundException(receiverId));
        Meet meet = meetRepository.findById(meetId).orElseThrow(() -> new MeetIdNotFoundException(meetId));

        // 쪽지의 chatroom 설정
        Long count1 = messageRepository.countBySender_IdAndReceiver_IdAndMeet_Id(senderId, receiverId, meetId);
        Long count2 = messageRepository.countBySender_IdAndReceiver_IdAndMeet_Id(receiverId, senderId, meetId);
        Chatroom chatroom;
        if (count1.equals(0L) && count2.equals(0L)) { // 새로 채팅방을 생성할 경우
            Chatroom newChatroom = new Chatroom();
            chatroom = chatroomRepository.save(newChatroom);
        } else if (count1.equals(0L)) {
            chatroom = messageRepository.findTopBySender_IdAndReceiver_IdAndMeet_Id(receiverId, senderId, meetId)
                    .getChatroom();
        } else {
            chatroom = messageRepository.findTopBySender_IdAndReceiver_IdAndMeet_Id(senderId, receiverId, meetId)
                    .getChatroom();
        }

        Message newMessage = Message.builder()
                .chatroom(chatroom)
                .sender(sender)
                .receiver(receiver)
                .meet(meet)
                .content(messageCreateRequest.getContent())
                .build();

        messageRepository.save(newMessage);

        return newMessage.getId();
    }

    /**
     * 쪽지 채팅방 내부의 쪽지 리스트를 조회해 반환한다.
     *
     * @param chatroomId 쪽지 채팅방의 id
     * @param memberId   현재 로그인한 사용자 id
     * @return 조회한 쪽지 DTO 리스트를 반환한다.
     */
    public List<MessageDto> findMessages(Long chatroomId, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberIdNotFoundException(memberId));

        // 채팅방 내부의 쪽지 리스트를 가져온다.
        List<Message> messages = messageRepository.findAllByChatroom_IdOrderByCreatedAt(chatroomId);

        if (messages.isEmpty()) {
            throw new ChatroomIdNotFoundException(chatroomId);
        }
        // 사용자가 채팅방을 볼 권한이 없을 경우 예외가 발생한다.
        else if (member != messages.get(0).getSender() && member != messages.get(0).getReceiver()) {
            throw new MessageGetForbiddenException(chatroomId, memberId);
        }

        // 쪽지 리스트를 쪽지 DTO 리스트로 변환하여 반환한다.
        return messages.stream()
                .map(MessageDto::from)
                .collect(Collectors.toUnmodifiableList());
    }
}
