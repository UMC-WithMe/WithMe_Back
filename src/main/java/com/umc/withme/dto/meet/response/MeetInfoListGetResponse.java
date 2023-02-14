package com.umc.withme.dto.meet.response;

import com.umc.withme.dto.meet.MeetDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 모임 모집글 조회 및 모임 기록 조회에서 사용되는 Response DTO
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MeetInfoListGetResponse {

    @Schema(description = "조회한 모임 정보 리스트")
    private List<MeetInfoResponse> meetInfoResponses;

    public static MeetInfoListGetResponse from(List<MeetDto> meetDtos) {
        return new MeetInfoListGetResponse(
                meetDtos.stream()
                        .map(MeetInfoResponse::from)
                        .collect(Collectors.toUnmodifiableList())
        );
    }
}
