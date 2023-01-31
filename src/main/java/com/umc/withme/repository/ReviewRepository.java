package com.umc.withme.repository;

import com.umc.withme.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Long countByReceiver_Id(Long receiverId);
}
