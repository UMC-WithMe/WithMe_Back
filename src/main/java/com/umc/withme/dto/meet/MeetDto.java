package com.umc.withme.dto.meet;

import com.umc.withme.domain.Address;
import com.umc.withme.domain.ImageFile;
import com.umc.withme.domain.Meet;
import com.umc.withme.domain.Member;
import com.umc.withme.domain.constant.MeetCategory;
import com.umc.withme.domain.constant.MeetStatus;
import com.umc.withme.domain.constant.RecruitStatus;
import com.umc.withme.dto.ImageFileDto;
import com.umc.withme.dto.address.AddressDto;
import com.umc.withme.dto.member.MemberDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MeetDto {

    private Long meetId;
    private MemberDto leader;  // 이 모임의 리더
    private ImageFileDto meetImage;
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

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    // TODO: 추후 좋아요 수 및 모임 인원 수 설정 필요
    public static MeetDto of(Long meetId, MemberDto leader, ImageFileDto meetImage, List<AddressDto> addresses, MeetCategory meetCategory, RecruitStatus recruitStatus, MeetStatus meetStatus, String title, String link, String content, Integer minPeople, Integer maxPeople, LocalDate startDate, LocalDate endDate, Long likeCount, Long membersCount, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        return new MeetDto(meetId, leader, meetImage, addresses, meetCategory, recruitStatus, meetStatus, title, link, content, minPeople, maxPeople, startDate, endDate, likeCount, membersCount, createdAt, modifiedAt);
    }

    // TODO: 추후 좋아요 수 및 모임 인원 수 설정 필요
    public static MeetDto of(List<AddressDto> addresses, MeetCategory meetCategory, String title, String link, String content, int minPeople, int maxPeople) {
        return MeetDto.of(null, null, null, addresses, meetCategory, null, null, title, link, content, minPeople, maxPeople, null, null, 0L, 1L, null, null);
    }

    // TODO: 추후 좋아요 수 및 모임 인원 수 설정 필요
    public static MeetDto from(
            Meet meet,
            List<Address> addresses, // meet에 등록된 모든 Address Entity List
            Member leader           // meet의 리더 멤버
    ) {
        return MeetDto.of(
                meet.getId(),
                MemberDto.from(leader),
                ImageFileDto.from(meet.getMeetImage()),
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
                0L,
                1L,
                meet.getCreatedAt(),
                meet.getModifiedAt()
        );
    }

    // 리뷰 조회 API에서 사용: leader와 address가 필요없어 null로 넣어둠
    // TODO: leader, address를 null로 처리하는 것이 괜찮을지, 다른 방법은 없는지에 대한 고민 필요
    // TODO: 추후 좋아요 수 및 모임 인원 수 설정 필요
    public static MeetDto from(Meet meet) {
        return MeetDto.of(
                meet.getId(),
                null,
                ImageFileDto.from(meet.getMeetImage()),
                null,
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
                0L,
                1L,
                meet.getCreatedAt(),
                meet.getModifiedAt()
        );
    }

    public Meet toEntity(ImageFile meetImage) {
        return Meet.builder()
                .meetImage(meetImage)
                .category(this.getMeetCategory())
                .title(this.getTitle())
                .minPeople(this.getMinPeople())
                .maxPeople(this.getMaxPeople())
                .link(this.getLink())
                .content(this.getContent())
                .build();
    }
}