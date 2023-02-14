package com.umc.withme.dto.message.response;

import com.umc.withme.dto.member.response.MemberShortInfoResponse;
import com.umc.withme.dto.message.MessageDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(description = "쪽지 정보 (1개) 응답 데이터 ")
public class MessageInfoResponse {

    @Schema(description = "쪽지 id(pk)")
    private Long messageId;
    @Schema(description = "쪽지 작성자 프로필 정보")
    private MemberShortInfoResponse memberShortInfoResponse;
    @Schema(description = "쪽지 내용")
    @Size(max = 500)
    private String content;
    @Schema(example = "2023-01-30T11:30:53.690336", description = "쪽지 작성 시간")
    private LocalDateTime createdAt;

    public static MessageInfoResponse of(Long messageId, MemberShortInfoResponse memberShortInfoResponse, String content, LocalDateTime createdAt) {
        return new MessageInfoResponse(messageId, memberShortInfoResponse, content, createdAt);
    }

    public static MessageInfoResponse from(MessageDto messageDto) {
        return of(
                messageDto.getMessageId(),
                MemberShortInfoResponse.from(messageDto.getSender()),
                messageDto.getContent(),
                null
        );
    }
}
