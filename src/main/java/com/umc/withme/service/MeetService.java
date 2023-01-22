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
import java.util.Optional;

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

    /**
     * 삭제할 모임의 id를 입력받아 해당 모임이 존재할 경우 삭제한다.
     * Meet, MeetAddress, MeetMember 테이블에서 삭제가 이루어진다.
     * @param meetId
     */
    @Transactional
    public void deleteMeetById(Long meetId){
        meetAddressRepository.findByMeet_Id(meetId)
                .stream()
                .forEach(ma -> meetAddressRepository.delete(ma));

        meetMemberRepository.findByMeet_Id(meetId)
                .stream()
                .forEach(mm -> meetMemberRepository.delete(mm));

        meetRepository.delete(
                meetRepository.findById(meetId)
                        .orElseThrow(EntityNotFoundException::new)
        );
    }
}


