package com.umc.withme.service;

import com.umc.withme.domain.Chatroom;
import com.umc.withme.domain.Meet;
import com.umc.withme.domain.Member;
import com.umc.withme.domain.Message;
import com.umc.withme.dto.member.MemberDto;
import com.umc.withme.dto.member.MemberShortInfoResponse;
import com.umc.withme.dto.meet.MeetDto;
import com.umc.withme.dto.message.MessageCreateRequest;
import com.umc.withme.dto.message.MessageInfoResponse;
import com.umc.withme.dto.message.MessageDto;
import com.umc.withme.exception.chatroom.ChatroomIdNotFoundException;
import com.umc.withme.exception.meet.MeetIdNotFoundException;
import com.umc.withme.exception.member.MemberIdNotFoundException;
import com.umc.withme.exception.message.MessageByChatroomIdNotFoundException;
import com.umc.withme.exception.message.MessageGetForbiddenException;
import com.umc.withme.repository.ChatroomRepository;
import com.umc.withme.repository.MeetRepository;
import com.umc.withme.repository.MemberRepository;
import com.umc.withme.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MessageService {

    private final MeetRepository meetRepository;
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

        // 쪽지의 sender, receiver, meet 엔티티 조회
        Member sender = memberRepository.findById(senderId).orElseThrow(() -> new MemberIdNotFoundException(senderId));
        Member receiver = memberRepository.findById(receiverId).orElseThrow(() -> new MemberIdNotFoundException(receiverId));
        Meet meet = meetRepository.findById(meetId).orElseThrow(() -> new MeetIdNotFoundException(meetId));
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
        // 쪽지의 chatroom 설정
        Boolean exists1 = messageRepository.existsBySender_IdAndReceiver_IdAndMeet_Id(senderId, receiverId, meetId);
        Boolean exists2 = messageRepository.existsBySender_IdAndReceiver_IdAndMeet_Id(receiverId, senderId, meetId);
        Chatroom chatroom;
        if (!exists1 && !exists2) { // 새로 채팅방을 생성할 경우
            Chatroom newChatroom = new Chatroom();
            chatroom = chatroomRepository.save(newChatroom);
        } else if (exists1) {
            chatroom = messageRepository.findTopBySender_IdAndReceiver_IdAndMeet_Id(senderId, receiverId, meetId)
                    .getChatroom();
        } else {
            chatroom = messageRepository.findTopBySender_IdAndReceiver_IdAndMeet_Id(receiverId, senderId, meetId)
                    .getChatroom();
        }

        // 쪽지 생성
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
     * 쪽지함 조회 (최신순)
     *
     * @param memberId 쪽지함을 조회하려는 사용자의 id. (현재 로그인한 사용자 id)
     * @return 쪽지 정보 응답 데이터 (상대방 프로필, 마지막 쪽지 내용, 작성시간) 리스트
     */
    public List<MessageInfoResponse> getMessageList(Long memberId) {
        List<MessageInfoResponse> messageInfoResponseList = new ArrayList<>();

        // 쪽지 리스트 받아오기
        List<Message> messageList = messageRepository.findAllByMemberId(memberId);

        for (Message message : messageList) {
            // 쪽지 작성자 엔티티 조회
            Member member = memberRepository.findById(message.getCreatedBy())
                    .orElseThrow(() -> new MemberIdNotFoundException(message.getCreatedBy()));

            // 쪽지 DTO 리스트에 추가
            messageInfoResponseList.add(
                    MessageInfoResponse.of(
                            message.getId(),
                            MemberShortInfoResponse.from(MemberDto.from(member)),
                            message.getContent(),
                            message.getCreatedAt()
                    ));
        }
        return messageInfoResponseList;
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
        if (member != messages.get(0).getSender() && member != messages.get(0).getReceiver()) {
            throw new MessageGetForbiddenException(chatroomId, memberId);
        }

        // 쪽지 리스트를 쪽지 DTO 리스트로 변환하여 반환한다.
        return messages.stream()
                .map(MessageDto::from)
                .collect(Collectors.toUnmodifiableList());
    }

    /**
     * 채팅방에 연결된 모임을 조회해 DTO로 반환한다.
     *
     * @param chatroomId 조회하려는 채팅방 id
     * @return 조회한 모임 DTO
     */
    public MeetDto findMeetByChatroomId(Long chatroomId) {
        Message message = messageRepository.findFirstByChatroom_Id(chatroomId)
                .orElseThrow(() -> new MessageByChatroomIdNotFoundException(chatroomId));
        return MeetDto.from(message.getMeet());
    }
}
