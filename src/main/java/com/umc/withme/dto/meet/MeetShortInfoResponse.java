package com.umc.withme.dto.meet;


import com.umc.withme.domain.constant.RecruitStatus;
import com.umc.withme.dto.member.MemberDto;
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

    //@Schema(description = "모집글 대표 사진")

    @Schema(description = "작성자 닉네임")
    private String nickname;

    @Schema(description = "모집글 제목")
    private String title;

    @Schema(description = "모집상태")
    private RecruitStatus recruitStatus;

    @Schema(description = "작성시간")
    private LocalDateTime createdAt;

    @Schema(description = "찜 수")
    private Long meetLikeCount;

    public static MeetShortInfoResponse of(MeetDto meetDto, MemberDto memberDto, Long meetLikeCount) {

        return new MeetShortInfoResponse(
                meetDto.getMeetId(),
                memberDto.getNickname(),
                meetDto.getTitle(),
                meetDto.getRecruitStatus(),
                meetDto.toEntity().getCreatedAt(),
                meetLikeCount
        );
    }

}
