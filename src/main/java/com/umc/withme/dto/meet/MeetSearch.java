package com.umc.withme.dto.meet;

import com.umc.withme.domain.constant.MeetCategory;
import com.umc.withme.domain.constant.MeetStatus;
import com.umc.withme.dto.address.AddressRequest;
import lombok.*;

/**
 * 모임 모집글 / 모임 기록 조회 시 사용하는 검색 데이터
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MeetSearch {

    private MeetCategory meetCategory;  // 모임 카테고리
    private AddressRequest adderess;   // 모임 동네 주소
    private String title;   // 모임 제목
    private MeetStatus meetStatus;  // 모임 진행상태
    private Long memberId;  // 사용자 id

    public static MeetSearch of(MeetCategory category, String sido, String sgg, String title) {
        return new MeetSearch(
                category != null ? category : null,
                sido != null && sgg != null ? AddressRequest.of(sido, sgg) : null,
                title,
                null,
                null);
    }

    public static MeetSearch of(MeetStatus meetStatus, Long memberId) {
        return new MeetSearch(null,
                null,
                null,
                meetStatus,
                memberId);
    }
}
