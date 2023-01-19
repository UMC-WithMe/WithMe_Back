package com.umc.withme.repository;

import com.umc.withme.domain.MeetAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeetAddressRepository extends JpaRepository<MeetAddress, Long> {

    /**
     * 해당 주소에 해당하는 MeetAddress 목록을 조회하는 함수
     * @param addressId
     * @return 해당 주소의 MeetAddress 리스트
     */
    List<MeetAddress> findAllByAddressId(Long addressId);

    /**
     * 해당 모임에 해당하는 MeetAddress 목록을 조회하는 함수
     * @param meetId
     * @return 해당 모임의 MeetAddress 리스트
     */
    List<MeetAddress> findAllByMeetId(Long meetId);

}
