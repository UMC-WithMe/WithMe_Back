package com.umc.withme.service;

import com.umc.withme.domain.Address;
import com.umc.withme.domain.Meet;
import com.umc.withme.domain.Member;
import com.umc.withme.domain.Message;
import com.umc.withme.dto.address.AddressDto;
import com.umc.withme.dto.message.MessageCreateRequest;
import com.umc.withme.exception.address.AddressNotFoundException;
import com.umc.withme.exception.meet.MeetIdNotFoundException;
import com.umc.withme.exception.member.MemberIdNotFoundException;
import com.umc.withme.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MessageService {

    private final MeetRepository meetRepository;
    private final MeetAddressRepository meetAddressRepository;
    private final AddressRepository addressRepository;
    private final MessageRepository messageRepository;
    private final MemberRepository memberRepository;

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

    private Address getAddress(AddressDto addressDto){
        return addressRepository.findBySidoAndSgg(
                addressDto.getSido(), addressDto.getSgg())
                .orElseThrow(()-> new AddressNotFoundException(addressDto.getSido(), addressDto.getSgg()));
    }
}
