package com.umc.withme.service;

import com.umc.withme.domain.*;
import com.umc.withme.dto.address.AddressDto;
import com.umc.withme.dto.meet.MeetDto;
import com.umc.withme.exception.address.AddressNotFoundException;
import com.umc.withme.exception.common.UnauthorizedException;
import com.umc.withme.exception.meet.MeetIdNotFoundException;
import com.umc.withme.exception.member.NicknameNotFoundException;
import com.umc.withme.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

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
     * MeetDto와 leaderName 입력받아 MeetRepository 및 MeetAddressRepository에 저장한다.
     *
     * @param meetDto    생성하고자 하는 모임 모집글 정보
     * @param leaderName 생성하고자 하는 모임 모집글의 글쓴이 (현재 로그인한 사용자)
     * @return 생성한 모임의 id
     */
    @Transactional
    public Long createMeet(MeetDto meetDto, String leaderName) {
        Member leader = memberRepository.findByNickname(leaderName)
                .orElseThrow(UnauthorizedException::new);

        Meet meet1 = meetDto.toEntity(leader);

        Meet meet = meetRepository.save(meet1);

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
     * 모임 id를 입력받아 모임 DTO를 컨트롤러에게 반환하는 함수이다.
     *
     * @param meetId 조회할 모임의 id
     * @return 조회된 모임의 DTO
     */
    public MeetDto findById(Long meetId) {
        Meet meet = meetRepository.findById(meetId)
                .orElseThrow(() -> new MeetIdNotFoundException(meetId));

        List<Address> addresses = meetAddressRepository.findAllByMeet_Id(meetId)
                .stream()
                .map(ma -> ma.getAddress())
                .collect(Collectors.toUnmodifiableList());

        Member member = memberRepository.findById(meet.getCreatedBy())
                .orElseThrow(UnauthorizedException::new);

        return MeetDto.from(meet, addresses, member);
    }

    /**
     * 모임을 meetDto 를 바탕으로 수정한다.
     * 수정하려는 사용자가 기존 모임의 리더(주인)과 동일해야 모임을 수정할 수 있다.
     *
     * @param meetId     수정하려는 모임의 id
     * @param memberName 수정하려는 사용자
     * @param meetDto    수정하려는 정보가 담긴 dto
     * @return 수정된 모임 DTO
     */
    @Transactional
    public MeetDto updateById(Long meetId, String memberName, MeetDto meetDto) {
        // 수정하려는 모임 조회
        Meet meet = meetRepository.findById(meetId)
                .orElseThrow(() -> new MeetIdNotFoundException(meetId));

        // 모임의 리더
        Member meetLeader = memberRepository.findById(meet.getCreatedBy())
                .orElseThrow(UnauthorizedException::new);

        // 수정을 시도하는 사용자
        Member member = memberRepository.findByNickname(memberName)
                .orElseThrow(NicknameNotFoundException::new);

        // 수정하고자 하는 모임의 주인과 사용자가 일치하는지 확인인하고 모임 수정
        if (meetLeader.equals(member)) {
            // 모임의 MeetAddress 모두 삭제 후 meetDto에 따라 다시 생성
            updateMeetAddress(meetId, meetDto, meet);

            // 모임 엔티티 수정
            meet.update(meetDto);

            // 수정된 모임의 주소 리스트 받아오기
            List<Address> addresses = meetAddressRepository.findAllByMeet_Id(meetId)
                    .stream()
                    .map(MeetAddress::getAddress)
                    .collect(Collectors.toList());

            // 수정된 모임 정보 바탕으로 MeetDto 생성해서 반환
            return MeetDto.from(
                    meet,
                    addresses,
                    member);
        } else
            throw new EntityNotFoundException();
    }

    private void updateMeetAddress(Long meetId, MeetDto meetDto, Meet meet) {
        List<MeetAddress> meetAddresses = meetAddressRepository.findAllByMeet_Id(meetId);
        for (MeetAddress meetAddress : meetAddresses) {
            meetAddressRepository.delete(meetAddress);
        }
        meetDto.getAddresses().forEach(addressDto -> {
            Address address = getAddress(addressDto);
            meetAddressRepository.save(new MeetAddress(meet, address));
        });
    }
}


