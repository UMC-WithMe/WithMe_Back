package com.umc.withme.dto.Meet;

import com.umc.withme.domain.Address;
import com.umc.withme.domain.Meet;
import com.umc.withme.domain.MeetAddress;
import com.umc.withme.domain.Member;
import com.umc.withme.domain.constant.MeetCategory;
import com.umc.withme.domain.constant.MeetStatus;
import com.umc.withme.domain.constant.RecruitStatus;
import com.umc.withme.repository.MeetAddressRepository;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class MeetDto {

    @Autowired
    private static MeetAddressRepository meetAddressRepository;
    private Long meetId;
    private MeetLeaderDto leader;  // 이 모임의 리더
    private List<MeetAddressDto> addresses;  // 이 모임에 설정된 동네 리스트
    private MeetCategory meetCategory;
    private RecruitStatus recruitStatus;
    private MeetStatus meetStatus;

    private String title;
    private Integer minPeople;
    private Integer maxPeople;
    private String link;
    private String content;
    private LocalDate startDate;
    private LocalDate endDate;

    private String printAddress() {
        String result = "";
        if(getAddresses() == null || getAddresses().isEmpty()) return result;

        result += "addresses={";
        for (MeetAddressDto address : getAddresses()) {
            result += address.toString() + ", ";
        }
        result += "}\n";
        return result;
    }

    /**
     * Meet Entity를 입력받아
     * MeetDto를 반환하는 함수
     *
     * @param 변환할 Meet Entity
     * @return 변환된 MeetDto
     */
    public static MeetDto from(Meet meet){
        MeetDto result = MeetDto.builder()
                .meetId(meet.getId())
                .leader(MeetLeaderDto.from(meet.getLeader()))
                .addresses(new ArrayList<>())
                .meetCategory(meet.getCategory())
                .recruitStatus(meet.getRecruitStatus())
                .meetStatus(meet.getMeetStatus())
                .title(meet.getTitle())
                .minPeople(meet.getMinPeople())
                .maxPeople(meet.getMaxPeople())
                .link(meet.getLink())
                .content(meet.getContent())
                .startDate(meet.getStartDate())
                .endDate(meet.getEndDate())
                .build();
        return result;
    }

    /**
     * MeetDto 객체를 Meet Entity로 변환해서 반환해주는 함수이다.
     * @return Meet Entity
     */
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

    /**
     * MeetDto를 생성해서 반환해주는 함수이다.
     * @param meetId
     * @param leader
     * @param meetCategory
     * @param recruitStatus
     * @param meetStatus
     * @param title
     * @param minPeople
     * @param maxPeople
     * @param link
     * @param content
     * @param startDate
     * @param endDate
     * @param addresses (Meet의 동네 목록 리스트)
     * @return 생성된 MeetDto 인스턴스
     */
    public static MeetDto of(
            Long meetId, Member leader, MeetCategory meetCategory,
            RecruitStatus recruitStatus, MeetStatus meetStatus,
            String title, int minPeople, int maxPeople, String link,
            String content, LocalDate startDate, LocalDate endDate,
            List<Address> addresses){
        MeetDto meetDto = MeetDto.builder()
                .meetId(meetId)
                .leader(MeetLeaderDto.from(leader))
                .addresses(new ArrayList<>())
                .meetCategory(meetCategory)
                .recruitStatus(recruitStatus)
                .meetStatus(meetStatus)
                .title(title)
                .minPeople(minPeople)
                .maxPeople(maxPeople)
                .link(link)
                .content(content)
                .startDate(startDate)
                .endDate(endDate)
                .build();
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
