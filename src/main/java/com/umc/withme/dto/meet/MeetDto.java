package com.umc.withme.dto.meet;

import com.umc.withme.domain.Address;
import com.umc.withme.domain.Meet;
import com.umc.withme.domain.Member;
import com.umc.withme.domain.constant.MeetCategory;
import com.umc.withme.domain.constant.MeetStatus;
import com.umc.withme.domain.constant.RecruitStatus;
import com.umc.withme.dto.address.AddressDto;
import com.umc.withme.dto.member.MemberDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MeetDto {

    private Long meetId;
    private MemberDto leader;  // 이 모임의 리더
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

    private Long likeCount; // 이 모임의 좋아요 수
    private Long membersCount;   // 이 모임에 모집된 사람의 수

    public static MeetDto from(
            Meet meet,
            List<Address> addresses, // meet에 등록된 모든 Address Entity List
            Member leader,           // meet의 리더 멤버
            Long likeCount,
            Long membersCount
    ) {
        return new MeetDto(
                meet.getId(),
                MemberDto.from(leader),
                addresses.stream()
                        .map(AddressDto::from)
                        .collect(Collectors.toUnmodifiableList()),
                meet.getCategory(),
                meet.getRecruitStatus(),
                meet.getMeetStatus(),
                meet.getTitle(),
                meet.getLink(),
                meet.getContent(),
                meet.getMinPeople(),
                meet.getMaxPeople(),
                meet.getStartDate(),
                meet.getEndDate(),
                likeCount,
                membersCount);
    }

    public static MeetDto of(Long meetId, MemberDto leader, MeetCategory meetCategory, RecruitStatus recruitStatus, MeetStatus meetStatus, String title, String link, String content, int minPeople, int maxPeople, LocalDate startDate, LocalDate endDate, List<AddressDto> addresses, Long likeCount, Long membersCount) {
        return new MeetDto(meetId, leader, addresses, meetCategory, recruitStatus, meetStatus, title, link, content, minPeople, maxPeople, startDate, endDate, likeCount, membersCount);
    }

    public static MeetDto of(MemberDto leader, MeetCategory meetCategory, String title, String link, String content, int minPeople, int maxPeople, List<AddressDto> addresses, Long likeCount, Long membersCount) {
        return MeetDto.of(null, leader, meetCategory, null, null, title, link, content, minPeople, maxPeople, null, null, addresses, likeCount, membersCount);
    }

    public Meet toEntity() {
        return Meet.builder()
                .category(this.getMeetCategory())
                .title(this.getTitle())
                .minPeople(this.getMinPeople())
                .maxPeople(this.getMaxPeople())
                .link(this.getLink())
                .content(this.getContent())
                .build();
    }
}