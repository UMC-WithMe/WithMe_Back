package com.umc.withme.repository;

import com.umc.withme.domain.MeetAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeetAddressRepository extends JpaRepository<MeetAddress, Long> {

    List<MeetAddress> findAllByMeet_Id(Long meetId);

    void deleteAllByMeet_Id(Long meetId);
}
