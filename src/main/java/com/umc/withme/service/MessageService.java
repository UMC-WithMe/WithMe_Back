package com.umc.withme.service;

import com.umc.withme.domain.Meet;
import com.umc.withme.domain.Member;
import com.umc.withme.domain.Message;
import com.umc.withme.dto.message.MessageCreateRequest;
import com.umc.withme.dto.message.MessageInfoResponse;
import com.umc.withme.exception.meet.MeetIdNotFoundException;
import com.umc.withme.exception.member.MemberIdNotFoundException;
import com.umc.withme.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MessageService {

    private final MeetRepository meetRepository;
    private final MessageRepository messageRepository;
    private final MemberRepository memberRepository;
    private final ReviewService reviewService;
    /**
     * 새로운 쪽지 생성하여 DB에 저장
     *
     * @param senderId 쪽지 보낸 회원 id(pk)
     * @param receiverId 쪽지 받는 회원 id(pk)
     * @param meetId 관련 모집글 id(pk)
     * @param messageCreateRequest 쪽지 생성 요청 데이터 (쪽지 내용)
     * @return 생성된(DB에 저장된) 쪽지 id(pk)
     */
    @Transactional
    public Long createMessage(Long senderId, Long receiverId, Long meetId, MessageCreateRequest messageCreateRequest){

        Member sender = memberRepository.findById(senderId).orElseThrow();
        Member receiver = memberRepository.findById(receiverId).orElseThrow(()-> new MemberIdNotFoundException(senderId));

        Meet meet = meetRepository.findById(meetId).orElseThrow(()-> new MeetIdNotFoundException(meetId));

        Message newMessage = Message.builder()
                .sender(sender)
                .receiver(receiver)
                .meet(meet)
                .content(messageCreateRequest.getContent())
                .build();

        Message savedMessage = messageRepository.save(newMessage);

        return savedMessage.getId();
    }

    /**
     * 쪽지함 조회 (최신순)
     * @param memberId
     * @return 쪽지 정보 응답 데이터 (상대방 프로필, 마지막 쪽지 내용, 작성시간) 리스트
     */
    public List<MessageInfoResponse> getMessageList(Long memberId){
        List<MessageInfoResponse> messageInfoResponseList = new ArrayList<>();

       List<Message> messageList = messageRepository.findBySender_IdOrReceiver_Id(memberId, memberId);

        for(Message message: messageList) {
            Member you; Long youId;
            if(message.getSender().getId() == memberId){
                youId = message.getReceiver().getId();
                you = memberRepository.findById(youId).orElseThrow(()->new MemberIdNotFoundException(youId));
            }else{
                youId = message.getSender().getId();
                you = memberRepository.findById(youId).orElseThrow(()->new MemberIdNotFoundException(youId));
            }

        }
        return messageInfoResponseList;
    }
}
