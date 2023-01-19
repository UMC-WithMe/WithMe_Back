package com.umc.withme.service;

import com.umc.withme.domain.Address;
import com.umc.withme.domain.Meet;
import com.umc.withme.domain.MeetAddress;
import com.umc.withme.repository.AddressRepository;
import com.umc.withme.repository.MeetAddressRepository;
import com.umc.withme.repository.MeetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MeetService {

    private final MeetRepository meetRepository;
    private final MeetAddressRepository meetAddressRepository;
    private final AddressRepository addressRepository;

    /**
     * 새로운 Meet 엔티티와 Address 목록을 입력받아 모임 repository에 저장한다.
     * 그리고 MeetAddress repository에 meet과 address를 연결해 저장한다.
     * @param Meet Entity와 address Entity 목록
     */
    @Transactional
    public void createMeet(Meet meet, List<Address> addresses){
        meetRepository.save(meet);
        for (Address address : addresses) {
            meetAddressRepository.save(new MeetAddress(meet, address));
        }
    }
}


