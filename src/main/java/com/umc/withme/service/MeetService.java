package com.umc.withme.service;

import com.umc.withme.domain.*;
import com.umc.withme.dto.address.AddressDto;
import com.umc.withme.dto.meet.MeetDto;
import com.umc.withme.exception.address.AddressNotFoundException;
import com.umc.withme.exception.common.UnauthorizedException;
import com.umc.withme.exception.meet.MeetDeleteForbiddenException;
import com.umc.withme.exception.meet.MeetIdNotFoundException;
import com.umc.withme.exception.member.EmailNotFoundException;
import com.umc.withme.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
     * @param meetDto     생성하고자 하는 모임 모집글 정보
     * @param leaderEmail 생성하고자 하는 모임 모집글의 글쓴이 이메일 (현재 로그인한 사용자의 이메일)
     * @return 생성한 모임의 id
     */
    @Transactional
    public Long createMeet(MeetDto meetDto, String leaderEmail) {
        Member leader = memberRepository.findByEmail(leaderEmail)
                .orElseThrow(EmailNotFoundException::new);

        Meet meet = meetRepository.save(meetDto.toEntity());

        meetMemberRepository.save(new MeetMember(leader, meet));

        meetDto.getAddresses().forEach(addressDto -> {
            Address address = getAddress(addressDto);
            meetAddressRepository.save(new MeetAddress(meet, address));
        });

        return meet.getId();
    }

    /**
     * AddressDto를 Address Entity로 변환해 반환해준다.
     *
     * @param dto 변환할 AddressDto
     * @return 변환된 Address Entity
     */
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
     * 삭제할 모임의 id를 입력받아 해당 모임이 존재할 경우 삭제한다.
     * Meet, MeetAddress, MeetMember 테이블에서 삭제가 이루어진다.
     *
     * @param meetId        삭제할 모임의 id
     * @param loginMemberId 현재 로그인한 사용자의 id
     */
    @Transactional
    public void deleteMeetById(Long meetId, Long loginMemberId) {
        // 삭제하려는 모임
        Meet meet = meetRepository.findById(meetId)
                .orElseThrow(() -> new MeetIdNotFoundException(meetId));

        // 모임의 주인의 pk
        Long meetLeaderId = meet.getCreatedBy();

        // 모임의 주인과 사용자가 일치하면 해당 모임 삭제. 일치하지 않으면 예외 발생
        if (!meetLeaderId.equals(loginMemberId))
            throw new MeetDeleteForbiddenException(meet.getId(), loginMemberId);

        meetAddressRepository.findAllByMeet_Id(meetId)
                .forEach(ma -> meetAddressRepository.delete(ma));

        meetMemberRepository.findAllByMeet_Id(meetId)
                .forEach(mm -> meetMemberRepository.delete(mm));

        meetRepository.delete(meet);
    }
}
