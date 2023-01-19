package com.umc.withme.repository;

import com.umc.withme.domain.Meet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetRepository extends JpaRepository<Meet, Long> {
}
