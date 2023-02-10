package com.umc.withme.repository;

import com.umc.withme.domain.Meet;
import com.umc.withme.domain.MeetMember;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MeetMemberRepository extends JpaRepository<MeetMember, Long> {

    void deleteAllByMeet_Id(Long meetId);

    /**
     * 사용자 아이디를 받아 최근에 끝난 모임 2개를 조회
     *
     * @param memberId 사용자 아이디
     * @param pageable 모임 개수
     * @return
     */
    @Query("select mm.meet " +
            "from MeetMember mm " +
            "where mm.member.id = :memberId " +
            "order by mm.meet.endDate desc")
    List<Meet> findByMeetOrderByEndDate(@Param("memberId") Long memberId, Pageable pageable);
}
