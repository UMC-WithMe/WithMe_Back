package com.umc.withme.repository;

import com.umc.withme.domain.MeetLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeetLikeRepository extends JpaRepository<MeetLike, Long> {
<<<<<<< HEAD

    List<MeetLike> findAllByMember_Id(Long memberId);
    Long countByMeet_Id(Long meetId);

=======
    MeetLike findByMember_IdAndMeet_Id(Long memberId, Long meetId);
    boolean existByMember_IdAndMeet_Id(Long memberId, Long meetId);
>>>>>>> feature/#81-meet_like-create
}
