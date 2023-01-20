package com.umc.withme.dto.meet;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.umc.withme.domain.constant.MeetCategory;
import com.umc.withme.domain.constant.MeetStatus;
import com.umc.withme.domain.constant.RecruitStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.lang.Nullable;

import javax.persistence.GeneratedValue;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MeetCreateRequest {

    @NotEmpty
    private String leaderName;
    @NotEmpty
    private List<MeetAddressDto> addresses;
    private MeetCategory meetCategory;
    @Nullable
    private RecruitStatus recruitStatus;
    private MeetStatus meetStatus;

    @NotEmpty
    private String title;
    private String link;
    @NotEmpty
    private String content;
    @NotNull
    private Integer minPeople;
    @NotNull
    private Integer maxPeople;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull
    private LocalDate startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull
    private LocalDate endDate;

    public static MeetCreateRequest of(
            String leaderName, List<MeetAddressDto> addresses,
            MeetCategory meetCategory, RecruitStatus recruitStatus, MeetStatus meetStatus,
            String title, String link, String content, Integer minPeople, Integer maxPeople,
            LocalDate startDate, LocalDate endDate
    ){
        return new MeetCreateRequest(leaderName, addresses, meetCategory, recruitStatus,
                meetStatus, title, link, content, minPeople, maxPeople,
                startDate, endDate);
    }
}
