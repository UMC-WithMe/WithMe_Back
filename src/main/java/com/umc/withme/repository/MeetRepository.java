package com.umc.withme.repository;

import com.umc.withme.domain.Meet;
import com.umc.withme.dto.Meet.MeetDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeetRepository extends JpaRepository<Meet, Long> {
    /**
     * 제목으로 모임 조회
     * @param 검색할 제목
     * @return 반환할 Meet 목록
     */
    List<Meet> findMeetByTitle(String title);
}
