package com.umc.withme.repository;

import com.umc.withme.domain.Meet;
import com.umc.withme.dto.meet.MeetSearch;

import java.util.List;

public interface CustomizedMeetRepository {

    /**
     * 모임 전체 리스트 조회
     *
     * @param meetSearch 모임 리스트 필터 조건
     *                   1. 카테고리 2. 내동네/온동네 3.제목 4.모임진행상태
     * @return 조건에 해당하는 모임 엔티티 리스트
     */
    List<Meet> searchMeets(MeetSearch meetSearch);
}
