package com.umc.withme.repository;

import com.umc.withme.domain.Address;
import com.umc.withme.domain.Meet;
import com.umc.withme.domain.constant.MeetCategory;
import com.umc.withme.dto.Meet.MeetDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MeetRepository extends JpaRepository<Meet, Long> {
    /**
     * 제목으로 모임 조회
     * @param 검색할 제목
     * @return 해당 title을 가진 Meet 목록
     */
    List<Meet> findMeetsByTitle(String title);

    /**
     * 카테고리별로 모임 조회
     * @param category
     * @return 해당 카테고리인 Meet 목록
     */
    List<Meet> findMeetsByCategory(MeetCategory category);
}
