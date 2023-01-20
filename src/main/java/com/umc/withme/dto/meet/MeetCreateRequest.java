package com.umc.withme.dto.meet;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.umc.withme.domain.constant.MeetCategory;
import com.umc.withme.domain.constant.MeetStatus;
import com.umc.withme.domain.constant.RecruitStatus;
import com.umc.withme.dto.address.AddressDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MeetCreateRequest {

    @NotEmpty
    private String leaderName;
    @NotEmpty
    private List<AddressDto> addresses;
    private MeetCategory meetCategory;

    @NotEmpty
    private String title;
    private String link;
    @NotEmpty
    private String content;
    @NotNull
    private Integer minPeople;
    @NotNull
    private Integer maxPeople;

    public static MeetCreateRequest of(
            String leaderName, List<AddressDto> addresses,
            MeetCategory meetCategory,
            String title, String link, String content, Integer minPeople, Integer maxPeople
    ){
        return new MeetCreateRequest(leaderName, addresses, meetCategory, title, link, content, minPeople, maxPeople);
    }
}
