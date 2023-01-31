package com.umc.withme.dto.message;

import com.umc.withme.dto.member.MemberShortInfoResponse;
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

    @Schema(description = "쪽지 작성자 프로필 정보")
    private MemberShortInfoResponse memberShortInfoResponse;
    @Schema(description = "쪽지 내용") @Size(max = 500)
    private String content;
    @Schema(example = "2023-01-30T11:30:53.690336", description = "쪽지 작성 시간")
    private LocalDateTime createAt;

    public static MessageInfoResponse of(MemberShortInfoResponse memberShortInfoResponse, String content, LocalDateTime createAt){
        return new MessageInfoResponse(memberShortInfoResponse, content, createAt);
    }
}
