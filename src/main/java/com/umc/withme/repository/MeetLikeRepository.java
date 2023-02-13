package com.umc.withme.repository;

import com.umc.withme.domain.MeetLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MeetLikeRepository extends JpaRepository<MeetLike, Long> {
    Optional<MeetLike> findByMember_IdAndMeet_Id(Long memberId, Long meetId);
    boolean existsByMember_IdAndMeet_Id(Long memberId, Long meetId);
}
