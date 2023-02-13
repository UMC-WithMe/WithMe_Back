package com.umc.withme.dto.message;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 쪽지 채팅방 내부 쪽지 조회 시 사용되는 Response DTO
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class MessageInfoListGetResponse {

    @Schema(example = "1", description = "모임의 id")
    Long meetId;

    @Schema(example = "영어 스터디 같이 해요!", description = "모임 모집글의 제목")
    String meetTitle;

    @Schema(description = "조회한 쪽지 리스트")
    List<MessageInfoGetResponse> messageInfoGetResponses;

    public static MessageInfoListGetResponse from(Long meetId, String meetTitle, List<MessageDto> messageDtos) {
        return new MessageInfoListGetResponse(
                meetId,
                meetTitle,
                messageDtos.stream()
                        .map(MessageInfoGetResponse::from)
                        .collect(Collectors.toUnmodifiableList())
        );
    }
}
