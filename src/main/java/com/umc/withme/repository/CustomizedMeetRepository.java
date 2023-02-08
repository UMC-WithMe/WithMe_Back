package com.umc.withme.repository;

import com.umc.withme.domain.Meet;
import com.umc.withme.dto.meet.MeetRecordSearch;
import com.umc.withme.dto.meet.MeetSearch;

import java.util.List;

public interface CustomizedMeetRepository {

    /**
     * 모임 모집글 전체 리스트 조회
     *
     * @param meetSearch 모임 리스트 필터 조건
     *                   1. 카테고리 2. 내동네/온동네 3.제목
     * @return 조건에 해당하는 모임 엔티티 리스트
     */
    List<Meet> searchMeets(MeetSearch meetSearch);

    /**
     * 모임 기록 전체 리스트 조회
     *
     * @param meetRecordSearch 모임 기록 리스트 필터 조건
     *                         1. 모임 진행 상태 2. 사용자 id
     * @return 조건에 해당하는 모임 엔티티 리스트
     */
    List<Meet> searchMeetRecords(MeetRecordSearch meetRecordSearch);
}
