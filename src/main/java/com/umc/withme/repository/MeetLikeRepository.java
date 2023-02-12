package com.umc.withme.repository;

import com.umc.withme.domain.MeetLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetLikeRepository extends JpaRepository<MeetLike, Long> {
    MeetLike findByMember_IdAndMeet_Id(Long memberId, Long meetId);
    boolean existByMember_IdAndMeet_Id(Long memberId, Long meetId);
}
