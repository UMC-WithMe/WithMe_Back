package com.umc.withme.dto.meet;

import com.umc.withme.domain.constant.MeetCategory;
import com.umc.withme.dto.address.AddressRequest;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 모임 모집글 조회 시 사용하는 검색 데이터
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MeetSearch {

    private MeetCategory meetCategory;  // 모임 카테고리
    private AddressRequest adderess;   // 모임 동네 주소
    private String title;   // 모임 제목

    public static MeetSearch of(MeetCategory category, String sido, String sgg, String title) {
        return new MeetSearch(
                category != null ? category : null,
                sido != null && sgg != null ? AddressRequest.of(sido, sgg) : null,
                title);
    }
}
