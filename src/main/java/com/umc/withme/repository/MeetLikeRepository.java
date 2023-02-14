package com.umc.withme.repository;

import com.umc.withme.domain.MeetLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetLikeRepository extends JpaRepository<MeetLike, Long> {
    boolean existsByMember_IdAndMeet_Id(Long memberId, Long meetId);
}
