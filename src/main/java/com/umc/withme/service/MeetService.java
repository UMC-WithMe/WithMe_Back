package com.umc.withme.service;

import com.umc.withme.domain.Address;
import com.umc.withme.domain.Meet;
import com.umc.withme.domain.MeetAddress;
import com.umc.withme.domain.Member;
import com.umc.withme.dto.meet.MeetAddressDto;
import com.umc.withme.dto.meet.MeetDto;
import com.umc.withme.exception.address.AddressNotFoundException;
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
     * @param meetAddressDtos
     */
    @Transactional
    public MeetDto createMeet(MeetDto meetDto, List<MeetAddressDto> meetAddressDtos){
        // Member 엔티티인 리더를 찾아온다.
        Member leader = memberRepository.findByNickname(meetDto.getLeader().getNickName())
                .orElseThrow(NicknameNotFoundException::new);

        // meet을 리더를 설정하고 저장한다.
        Meet meet = meetDto.toEntity();
        meet.setLeader(leader);
        Meet savedMeet = meetRepository.save(meet);
        MeetDto savedMeetDto = MeetDto.from(savedMeet);

        // savedMeetDto 에 동네 목록 리스트 설정
        List<Address> addressList = new ArrayList<>();
        for (MeetAddressDto meetAddressDto : meetAddressDtos) {
            Optional<Address> optionalAddress = addressRepository.
                    findBySidoAndSgg(meetAddressDto.getSido(), meetAddressDto.getSgg());
            if(optionalAddress.isEmpty()) continue;
            Address address = optionalAddress.get();
            addressList.add(address);
            meetAddressRepository.save(new MeetAddress(savedMeet, address));
        }
        savedMeetDto.setAddresses(addressList);

        return savedMeetDto;
    }
}


