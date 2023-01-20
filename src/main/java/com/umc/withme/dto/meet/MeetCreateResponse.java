package com.umc.withme.dto.meet;

import com.umc.withme.domain.Address;
import com.umc.withme.domain.constant.MeetCategory;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MeetCreateResponse {

    private String leaderNickname;

    private String title;
    private String link;
    private String content;

    private MeetCategory meetCategory;
    private List<MeetAddressDto> addressses;
    private Integer minPeople;
    private Integer maxPeople;
    private LocalDate startDate;
    private LocalDate endDate;

    public static MeetCreateResponse from(MeetDto dto){
        return new MeetCreateResponse(
                dto.getLeader().getNickName(),
                dto.getTitle(),
                dto.getLink(),
                dto.getContent(),
                dto.getMeetCategory(),
                dto.getAddresses(),
                dto.getMinPeople(),
                dto.getMaxPeople(),
                dto.getStartDate(),
                dto.getEndDate()
        );
    }
}
