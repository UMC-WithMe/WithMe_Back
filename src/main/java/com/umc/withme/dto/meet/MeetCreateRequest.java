package com.umc.withme.dto.meet;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.umc.withme.domain.constant.MeetCategory;
import com.umc.withme.domain.constant.MeetStatus;
import com.umc.withme.domain.constant.RecruitStatus;
import com.umc.withme.dto.address.AddressDto;
import com.umc.withme.dto.address.AddressRequest;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MeetCreateRequest {

    @NotEmpty
    private List<AddressRequest> addresses;
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
            List<AddressRequest> addresses, MeetCategory meetCategory,
            String title, String link, String content,
            Integer minPeople, Integer maxPeople
    ) {
        return new MeetCreateRequest(addresses, meetCategory, title, link, content, minPeople, maxPeople);
    }

    public MeetDto toDto() {
        return MeetDto.of(
                null,
                getMeetCategory(),
                getTitle(),
                getLink(),
                getContent(),
                getMinPeople(),
                getMaxPeople(),
                getAddresses().stream()
                        .map(AddressRequest::toDto)
                        .collect(Collectors.toUnmodifiableList())
        );
    }
}
