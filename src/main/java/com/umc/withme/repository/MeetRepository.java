package com.umc.withme.repository;

import com.umc.withme.domain.Address;
import com.umc.withme.domain.Meet;
import com.umc.withme.domain.constant.MeetCategory;
import com.umc.withme.dto.Meet.MeetDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MeetRepository extends JpaRepository<Meet, Long> {
}
