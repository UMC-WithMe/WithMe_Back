package com.umc.withme.service;

import com.umc.withme.domain.*;
import com.umc.withme.dto.address.AddressDto;
import com.umc.withme.dto.meet.MeetDto;
import com.umc.withme.exception.address.AddressNotFoundException;
import com.umc.withme.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MeetService {

    private final MeetRepository meetRepository;
    private final MeetAddressRepository meetAddressRepository;
    private final MeetMemberRepository meetMemberRepository;
    private final AddressRepository addressRepository;
    private final MemberRepository memberRepository;

    /**
     * MeetDto 와 MeetAddressDto 리스트를 입력받아
     * MeetRepository 및 MeetAddressRepository에 저장한다.
     *
     * @param meetDto
     * @param addressDtos
     */
    @Transactional
    public Long createMeet(MeetDto meetDto, Long leaderId) {
        Member leader = memberRepository.findById(leaderId)
                .orElseThrow(EntityNotFoundException::new); // TODO : 추후 인증기능 구현되면 exception 교체해야된다.

        Meet meet = meetRepository.save(meetDto.toEntity(leader));

        meetMemberRepository.save(new MeetMember(leader, meet));

        meetDto.getAddresses().forEach(addressDto -> {
            Address address = getAddress(addressDto);
            meetAddressRepository.save(new MeetAddress(meet, address));
        });

        return meet.getId();
    }

    private Address getAddress(AddressDto dto) {
        return addressRepository.findBySidoAndSgg(dto.getSido(), dto.getSgg())
                .orElseThrow(() -> new AddressNotFoundException(dto.getSido(), dto.getSgg()));
    }
}


