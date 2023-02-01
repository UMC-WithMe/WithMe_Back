package com.umc.withme.service;

import com.umc.withme.domain.*;
import com.umc.withme.dto.address.AddressDto;
import com.umc.withme.dto.meet.MeetDto;
import com.umc.withme.exception.address.AddressNotFoundException;
import com.umc.withme.exception.meet.MeetDeleteForbiddenException;
import com.umc.withme.exception.meet.MeetIdNotFoundException;
import com.umc.withme.exception.meet.MeetUpdateForbiddenException;
import com.umc.withme.exception.member.EmailNotFoundException;
import com.umc.withme.exception.member.MemberIdNotFoundException;
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
                .map(MeetAddress::getAddress)
                .collect(Collectors.toUnmodifiableList());

        Member member = memberRepository.findById(meet.getCreatedBy())
                .orElseThrow(MemberIdNotFoundException::new);

        return MeetDto.from(meet, addresses, member);
    }

    /**
     * 모임을 meetDto 를 바탕으로 수정한다.
     * 수정하려는 사용자가 기존 모임의 리더(주인)과 동일해야 모임을 수정할 수 있다.
     *
     * @param meetId          수정하려는 모임의 id
     * @param loginMemberId   수정하려는 사용자의 id (현재 로그인한 사용자 pk)
     * @param meetDtoToUpdate 수정하려는 정보가 담긴 dto
     * @return 수정된 모임 DTO
     */
    @Transactional
    public MeetDto updateById(Long meetId, Long loginMemberId, MeetDto meetDtoToUpdate) {
        // 수정하려는 모임 엔티티 조회
        Meet meet = meetRepository.findById(meetId)
                .orElseThrow(() -> new MeetIdNotFoundException(meetId));

        // 모임의 리더 id
        Long meetLeaderId = meet.getCreatedBy();
        // 수정하고자 하는 모임의 주인과 사용자의 pk를 비교하고 일치하지 않으면 예외 처리
        if (!meetLeaderId.equals(loginMemberId)) {
            throw new MeetUpdateForbiddenException(meetId, loginMemberId);
        }

        // 모임의 MeetAddress 모두 삭제 후 meetDtoToUpdate에 따라 다시 생성
        meetAddressRepository.deleteAllByMeet_Id(meet.getId());
        meetDtoToUpdate.getAddresses().forEach(addressDto ->
                meetAddressRepository.save(new MeetAddress(meet, getAddress(addressDto))));

        // 모임 엔티티 수정
        meet.update(meetDtoToUpdate);

        // 수정된 모임의 주소 리스트 받아오기
        List<Address> addresses = meetAddressRepository.findAllByMeet_Id(meetId).stream()
                .map(MeetAddress::getAddress)
                .collect(Collectors.toList());

        // 수정된 모임 정보 바탕으로 MeetDto 생성해서 반환
        return MeetDto.from(
                meet,
                addresses,
                memberRepository.findById(meetLeaderId)
                        .orElseThrow(() -> new MemberIdNotFoundException(meetLeaderId))
        );
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

        // 모임의 주인과 사용자가 일치하면 해당 모임 삭제. 일치하지 않으면 예외 발생
        if (!meet.getCreatedBy().equals(loginMemberId)) {
            throw new MeetDeleteForbiddenException(meet.getId(), loginMemberId);
        }

        meetAddressRepository.deleteAllByMeet_Id(meetId);
        meetMemberRepository.deleteAllByMeet_Id(meetId);

        meetRepository.delete(meet);
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
}
