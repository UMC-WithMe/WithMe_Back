package com.umc.withme.repository;

import com.umc.withme.domain.MeetMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeetMemberRepository extends JpaRepository<MeetMember, Long> {

    void deleteAllByMeet_Id(Long meetId);

    List<MeetMember> findAllByMember_Id(Long memberId);
}
