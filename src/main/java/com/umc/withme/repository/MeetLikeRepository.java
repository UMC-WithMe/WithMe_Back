package com.umc.withme.repository;

import com.umc.withme.domain.MeetLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeetLikeRepository extends JpaRepository<MeetLike, Long> {

    List<MeetLike> findAllByMember_Id(Long receiverId);
    Long countByMeet_Id(Long meetId);

}
