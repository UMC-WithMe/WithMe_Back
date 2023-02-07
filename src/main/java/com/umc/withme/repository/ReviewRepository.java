package com.umc.withme.repository;

import com.umc.withme.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByReceiver_Id(Long receiverId);
}
