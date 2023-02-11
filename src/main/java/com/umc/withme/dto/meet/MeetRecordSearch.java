package com.umc.withme.dto.meet;

import com.umc.withme.domain.constant.MeetStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 모임 기록 조회 시 조회 시 사용하는 검색 데이터
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MeetRecordSearch {
    private MeetStatus meetStatus;  // 모임 진행상태 (현재 모임: PROGRESS, 지난 모임: COMPLETE)
    private Long memberId;  // 사용자 id

    public static MeetRecordSearch of(MeetStatus meetStatus, Long memberId) {
        return new MeetRecordSearch(meetStatus, memberId);
    }
}
