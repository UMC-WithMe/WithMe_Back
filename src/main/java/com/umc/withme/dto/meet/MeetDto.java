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
                .build();
    }

    public static MeetDto of(
            Long meetId, Member leader, MeetCategory meetCategory,
            RecruitStatus recruitStatus, MeetStatus meetStatus,
            String title, int minPeople, int maxPeople, String link,
            String content, LocalDate startDate, LocalDate endDate,
            List<Address> addresses){
        MeetDto meetDto = new MeetDto(meetId, MeetLeaderDto.from(leader), new ArrayList<>(),
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
}
