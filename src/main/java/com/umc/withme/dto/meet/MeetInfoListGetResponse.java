package com.umc.withme.dto.meet;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 모임 모집글 조회 및 모임 기록 조회에서 사용되는 Response DTO
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MeetInfoListGetResponse {

    @Schema(description = "조회한 모임 정보 리스트")
    private List<MeetInfoGetResponse> meetInfoGetResponses;

    public static MeetInfoListGetResponse from(List<MeetDto> meetDtos) {
        List<MeetInfoGetResponse> meetInfoGetResponseList = new ArrayList<>();
        for (MeetDto meetDto : meetDtos) {
            meetInfoGetResponseList.add(MeetInfoGetResponse.from(meetDto));
        }
        return new MeetInfoListGetResponse(meetInfoGetResponseList);
    }
}