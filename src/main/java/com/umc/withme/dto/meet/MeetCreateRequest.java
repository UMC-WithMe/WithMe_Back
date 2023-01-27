package com.umc.withme.dto.meet;

import com.umc.withme.domain.constant.MeetCategory;
import com.umc.withme.dto.address.AddressRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MeetCreateRequest {

    @NotEmpty
    private List<AddressRequest> addresses;

    @Schema(example = "STUDY", description = "모임의 카테고리")
    private MeetCategory meetCategory;

    @Schema(example = "OOO 스터디원 모집합니다!", description = "모임 모집글의 제목")
    @NotEmpty
    private String title;

    @Schema(example = "https://open.kakao.com/o/s1RrvrQe",
            description = "추가적으로 사용할 카톡 오픈채팅 링크 또는 모집 폼 링크")
    private String link;

    @Schema(
            example = "함께 OOO 공부 하고 자료도 나누면서 함께 성장해봐요!",
            description = "모임 모집글의 내용")
    @NotEmpty
    private String content;

    @Schema(example = "3", description = "모임의 최소 인원")
    @NotNull
    private Integer minPeople;

    @Schema(example = "5", description = "모임의 최대 인원")
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
