package com.umc.withme.dto.meet;

import com.umc.withme.domain.constant.MeetCategory;
import com.umc.withme.domain.constant.MeetStatus;
import com.umc.withme.domain.constant.RecruitStatus;
import com.umc.withme.dto.address.AddressDto;
import com.umc.withme.dto.member.MemberInfoGetResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MeetInfoGetResponse {

    private Long meetId;
    private MemberInfoGetResponse leader;  // 이 모임의 리더
    private List<AddressDto> addresses;  // 이 모임에 설정된 동네 리스트
    private MeetCategory meetCategory;
    private RecruitStatus recruitStatus;
    private MeetStatus meetStatus;

    private String title;
    private String link;
    private String content;

    private Integer minPeople;
    private Integer maxPeople;
    private LocalDate startDate;
    private LocalDate endDate;

    public static MeetInfoGetResponse from(MeetDto dto) {

        return new MeetInfoGetResponse(
                dto.getMeetId(),
                MemberInfoGetResponse.from(dto.getLeader()),
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
                dto.getEndDate());
    }
}
