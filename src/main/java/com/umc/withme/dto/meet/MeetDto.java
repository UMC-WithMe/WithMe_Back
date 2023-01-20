package com.umc.withme.dto.meet;

import com.umc.withme.domain.Address;
import com.umc.withme.domain.Meet;
import com.umc.withme.domain.Member;
import com.umc.withme.domain.constant.MeetCategory;
import com.umc.withme.domain.constant.MeetStatus;
import com.umc.withme.domain.constant.RecruitStatus;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MeetDto {

    private Long meetId;
    private MeetLeaderDto leader;  // 이 모임의 리더
    private List<MeetAddressDto> addresses;  // 이 모임에 설정된 동네 리스트
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

    public static MeetDto from(Meet meet){
        return new MeetDto(meet.getId(), MeetLeaderDto.from(meet.getLeader()), new ArrayList<>(),
                meet.getCategory(), meet.getRecruitStatus(), meet.getMeetStatus(),
                meet.getTitle(),  meet.getLink(), meet.getContent(),
                meet.getMinPeople(), meet.getMaxPeople(),
                meet.getStartDate(), meet.getEndDate());
    }

    public Meet toEntity(){
        return Meet.builder()
                .leader(this.getLeader().toEntity())
                .category(this.getMeetCategory())
                .title(this.getTitle())
                .minPeople(this.getMinPeople())
                .maxPeople(this.getMaxPeople())
                .link(this.getLink())
                .content(this.getContent())
                .startDate(this.startDate)
                .endDate(this.endDate)
                .build();
    }

    public static MeetDto of(
            Long meetId, Member leader, MeetCategory meetCategory,
            RecruitStatus recruitStatus, MeetStatus meetStatus,
            String title, String link, String content,
            int minPeople, int maxPeople,
            LocalDate startDate, LocalDate endDate,
            List<Address> addresses){
        MeetDto meetDto = new MeetDto(meetId, MeetLeaderDto.from(leader), new ArrayList<>(),
                meetCategory, recruitStatus, meetStatus,
                title, link, content, minPeople, maxPeople,
                startDate, endDate);
        meetDto.setAddresses(addresses);
        return meetDto;
    }

    public static MeetDto of(
            Member leader, MeetCategory meetCategory,
            RecruitStatus recruitStatus, MeetStatus meetStatus,
            String title, String link, String content,
            int minPeople, int maxPeople,
            LocalDate startDate, LocalDate endDate,
            List<Address> addresses){
        MeetDto meetDto = new MeetDto(MeetLeaderDto.from(leader), new ArrayList<>(),
                meetCategory, recruitStatus, meetStatus,
                title, link, content, minPeople, maxPeople,
                startDate, endDate);
        meetDto.setAddresses(addresses);
        return meetDto;
    }

    /**
     * 모임에 설정된 동네 목록을 설정하는 함수
     * @param addresses
     */
    public void setAddresses(List<Address> addresses){
        for (Address address : addresses) {
            this.addresses.add(MeetAddressDto.from(address));
        }
    }

    /**
     * meetId를 제외한 나머지 값들을 입력받는 생성자
     * @param leader
     * @param addresses
     * @param meetCategory
     * @param recruitStatus
     * @param meetStatus
     * @param title
     * @param link
     * @param content
     * @param minPeople
     * @param maxPeople
     * @param startDate
     * @param endDate
     */
    private MeetDto(MeetLeaderDto leader, List<MeetAddressDto> addresses, MeetCategory meetCategory, RecruitStatus recruitStatus, MeetStatus meetStatus, String title, String link, String content, Integer minPeople, Integer maxPeople, LocalDate startDate, LocalDate endDate) {
        this.leader = leader;
        this.addresses = addresses;
        this.meetCategory = meetCategory;
        this.recruitStatus = recruitStatus;
        this.meetStatus = meetStatus;
        this.title = title;
        this.link = link;
        this.content = content;
        this.minPeople = minPeople;
        this.maxPeople = maxPeople;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
