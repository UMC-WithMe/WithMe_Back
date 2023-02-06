package com.umc.withme.repository;

import com.umc.withme.domain.MeetMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetMemberRepository extends JpaRepository<MeetMember, Long> {

    void deleteAllByMeet_Id(Long meetId);
}
