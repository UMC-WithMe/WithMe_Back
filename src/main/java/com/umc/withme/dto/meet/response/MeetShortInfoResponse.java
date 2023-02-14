package com.umc.withme.dto.meet.response;


import com.umc.withme.domain.constant.MeetCategory;
import com.umc.withme.domain.constant.RecruitStatus;
import com.umc.withme.dto.ImageFileDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(description = "모집글리스트 조회 시 필요한 대표 정보")
public class MeetShortInfoResponse {
    @Schema(description = "모집글 id")
    private Long meetId;

    @Schema(description = "모임 대표 사진", example = "https://withme-s3-bucket.s3.ap-northeast-2.amazonaws.com/member/default-profile-image.jpeg")
    private ImageFileDto meetImage;

    @Schema(description = "작성자 닉네임")
    private String leaderNickname;

    @Schema(description = "카테고리")
    private MeetCategory meetCategory;

    @Schema(description = "모집글 제목")
    private String title;

    @Schema(description = "모집상태")
    private RecruitStatus recruitStatus;

    @Schema(description = "작성시간")
    private LocalDateTime createdAt;

    @Schema(description = "찜 수")
    private Integer meetLikeCount;

    public static MeetShortInfoResponse of(Long meetId, ImageFileDto meetImage, String leaderNickname, MeetCategory meetCategory, String title, RecruitStatus recruitStatus, LocalDateTime createdAt, Integer meetLikeCount) {
        return new MeetShortInfoResponse(meetId, meetImage, leaderNickname, meetCategory, title, recruitStatus, createdAt, meetLikeCount);
    }
}
