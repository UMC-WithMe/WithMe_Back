package com.umc.withme.dto.message;

import com.umc.withme.domain.Meet;
import com.umc.withme.domain.Member;
import com.umc.withme.domain.Message;
import com.umc.withme.dto.meet.MeetDto;
import com.umc.withme.dto.member.MemberDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageDto {

    private Long messageId;
    private MemberDto sender;
    private MemberDto receiver;
    private MeetDto meet;
    private String content;

    public static MessageDto of( MemberDto sender, MemberDto receiver, MeetDto meet, String content){
        return MessageDto.of(null, sender, receiver, meet, content);
    }
    public static MessageDto of(Long messageId, MemberDto sender, MemberDto receiver, MeetDto meet, String content){
        return new MessageDto(messageId, sender, receiver, meet, content);
    }

    public static MessageDto from(Message message){
        return new MessageDto(
                message.getId(),
                MemberDto.from(message.getSender()),
                MemberDto.from(message.getReceiver()),
                MeetDto.from(message.getMeet()),
                message.getContent());
    }

    public Message toEntity(Member sender, Member receiver, Meet meet){
        return Message.builder()
                .sender(sender)
                .receiver(receiver)
                .meet(meet)
                .content(this.getContent())
                .build();
    }
}
