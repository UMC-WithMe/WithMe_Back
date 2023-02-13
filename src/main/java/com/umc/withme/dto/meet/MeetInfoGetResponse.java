package com.umc.withme.dto.meet;

import com.umc.withme.domain.constant.MeetCategory;
import com.umc.withme.domain.constant.MeetStatus;
import com.umc.withme.domain.constant.RecruitStatus;
import com.umc.withme.dto.ImageFileDto;
import com.umc.withme.dto.address.AddressDto;
import com.umc.withme.dto.member.MemberInfoGetResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MeetInfoGetResponse {

    @Schema(example = "2", description = "조회한 모임의 id")
    private Long meetId;

    @Schema(description = "모임을 생성한 사용자 정보 (모임의 리더 정보)")
    private MemberInfoGetResponse leader;  // 이 모임의 리더

    @Schema(description = "모임 대표 사진", example = "https://withme-s3-bucket.s3.ap-northeast-2.amazonaws.com/member/default-profile-image.jpeg")
    private ImageFileDto meetImage;

    @Schema(description = "모임에 설정된 주소 리스트")
    private List<AddressDto> addresses;

    @Schema(example = "STUDY", description = "모임의 카테고리")
    private MeetCategory meetCategory;

    @Schema(example = "PROGRESS", description = "모임의 모집 상태 정보")
    private RecruitStatus recruitStatus;

    @Schema(example = "PROGRESS", description = "모임의 진행 정보")
    private MeetStatus meetStatus;

    @Schema(example = "OOO 스터디원 모집합니다!", description = "모임 모집글의 제목")
    private String title;

    @Schema(example = "https://open.kakao.com/o/s1RrvrQe",
            description = "추가적으로 사용할 카톡 오픈채팅 링크 또는 모집 폼 링크")
    private String link;

    @Schema(example = "함께 OOO 공부 하고 자료도 나누면서 함께 성장해봐요!", description = "모임 모집글의 내용")
    private String content;

    @Schema(example = "3", description = "모임의 최소 인원")
    private Integer minPeople;

    @Schema(example = "5", description = "모임의 최대 인원")
    private Integer maxPeople;

    @Schema(example = "2023-01-15", description = "모임의 시작 날짜")
    private LocalDate startDate;

    @Schema(example = "2023-02-15", description = "모임 마감(종료) 날짜")
    private LocalDate endDate;

    @Schema(example = "13", description = "이 모임 모집글의 좋아요 수")
    private Long likeCount;

    @Schema(example = "5", description = "이 모임에 현재 모집된 인원 수")
    private Long membersCount;

    public static MeetInfoGetResponse from(MeetDto dto) {

        return new MeetInfoGetResponse(
                dto.getMeetId(),
                MemberInfoGetResponse.from(dto.getLeader()),
                dto.getMeetImage(),
                dto.getAddresses(),
                dto.getMeetCategory(),
                dto.getRecruitStatus(),
                dto.getMeetStatus(),
                dto.getTitle(),
                dto.getLink(),
                dto.getContent(),
                dto.getMinPeople(),
                dto.getMaxPeople(),
                dto.getStartDate(),
                dto.getEndDate(),
                dto.getLikeCount(),
                dto.getMembersCount());
    }
}