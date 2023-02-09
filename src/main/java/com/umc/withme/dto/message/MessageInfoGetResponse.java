package com.umc.withme.dto.message;

import com.umc.withme.dto.member.MemberInfoGetResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageInfoGetResponse {

    @Schema(example = "1", description = "쪽지 id")
    private Long messageId;

    @Schema(description = "쪽지를 작성한 사용자 정보")
    private MemberInfoGetResponse sender;

    @Schema(description = "쪽지를 받은 사용자 정보")
    private MemberInfoGetResponse receiver;

    @Schema(example = "안녕하세요! 모임에 참가하고 싶어요!", description = "쪽지 내용")
    private String content;

    public static MessageInfoGetResponse from(MessageDto messageDto) {
        return new MessageInfoGetResponse(
                messageDto.getMessageId(),
                MemberInfoGetResponse.from(messageDto.getSender()),
                MemberInfoGetResponse.from(messageDto.getReceiver()),
                messageDto.getContent());
    }
}
