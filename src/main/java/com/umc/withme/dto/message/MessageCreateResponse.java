package com.umc.withme.dto.message;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageCreateResponse {

    private Long messageId;

    public static MessageCreateResponse of(Long messageId){
        return new MessageCreateResponse(messageId);
    }
}
