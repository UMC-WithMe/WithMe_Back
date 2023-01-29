package com.umc.withme.repository;

import com.umc.withme.domain.Meet;
import com.umc.withme.dto.meet.MeetSearch;

import java.util.List;

public interface CustomizedMeetRepository {

    // 모임 전체 리스트 조회
    // 1. 카테고리 2. 내동네/온동네 3. 제목 으로 검색
    List<Meet> takeAll(MeetSearch meetSearch);
}
