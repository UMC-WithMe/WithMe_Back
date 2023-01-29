package com.umc.withme.dto.message;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(description = "쪽지 생성 응답 데이터")
public class MessageCreateResponse {

    @Schema(description = "생성된 쪽지 id(pk)")
    private Long messageId;

    public static MessageCreateResponse of(Long messageId){
        return new MessageCreateResponse(messageId);
    }
}
