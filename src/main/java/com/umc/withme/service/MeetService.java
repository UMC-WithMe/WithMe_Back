package com.umc.withme.service;

import com.umc.withme.domain.Address;
import com.umc.withme.domain.Meet;
import com.umc.withme.domain.MeetAddress;
import com.umc.withme.domain.Member;
import com.umc.withme.dto.address.AddressDto;
import com.umc.withme.dto.meet.MeetDto;
import com.umc.withme.exception.member.NicknameNotFoundException;
import com.umc.withme.repository.AddressRepository;
import com.umc.withme.repository.MeetAddressRepository;
import com.umc.withme.repository.MeetRepository;
import com.umc.withme.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MeetService {

    private final MeetRepository meetRepository;
    private final MeetAddressRepository meetAddressRepository;
    private final AddressRepository addressRepository;
    private final MemberRepository memberRepository;

    /**
     * MeetDto 와 MeetAddressDto 리스트를 입력받아
     * MeetRepository 및 MeetAddressRepository에 저장한다.
     * @param meetDto
     * @param addressDtos
     */
    @Transactional
    public MeetDto createMeet(MeetDto meetDto, List<AddressDto> addressDtos){
        // Member 엔티티인 리더를 찾아온다.
        Member leader = memberRepository.findByNickname(meetDto.getLeader().getNickname())
                .orElseThrow(NicknameNotFoundException::new);

        // meet을 리더를 설정하고 저장한다.
        Meet meet = meetDto.toEntity();
        meet.setLeader(leader);
        Meet savedMeet = meetRepository.save(meet);
        MeetDto savedMeetDto = MeetDto.of(
                savedMeet.getId(), leader, savedMeet.getCategory(),
                savedMeet.getRecruitStatus(), savedMeet.getMeetStatus(),
                savedMeet.getTitle(), savedMeet.getLink(), savedMeet.getContent(),
                savedMeet.getMinPeople(), savedMeet.getMaxPeople(),
                savedMeet.getStartDate(), savedMeet.getEndDate(),
                addressDtos);

        // Meet와 Address 목록을 MeetAddress repository에 연결해 저장한다.
        List<Address> addressList = new ArrayList<>();
        for (AddressDto addressDto : addressDtos) {
            Optional<Address> optionalAddress = addressRepository.
                    findBySidoAndSgg(addressDto.getSido(), addressDto.getSgg());
            if(optionalAddress.isEmpty()) continue;
            Address address = optionalAddress.get();
            addressList.add(address);
            meetAddressRepository.save(new MeetAddress(savedMeet, address));
        }

        return savedMeetDto;
    }
}


