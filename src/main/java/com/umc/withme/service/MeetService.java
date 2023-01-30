package com.umc.withme.service;

import com.umc.withme.domain.*;
import com.umc.withme.dto.address.AddressDto;
import com.umc.withme.dto.meet.MeetDto;
import com.umc.withme.dto.meet.MeetSearch;
import com.umc.withme.exception.address.AddressNotFoundException;
import com.umc.withme.exception.common.UnauthorizedException;
import com.umc.withme.exception.meet.MeetDeleteForbiddenException;
import com.umc.withme.exception.meet.MeetIdNotFoundException;
import com.umc.withme.exception.member.EmailNotFoundException;
import com.umc.withme.exception.member.MemberIdNotFoundException;
import com.umc.withme.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

        //TODO : 좋아요 수 추후 구현 필요

        long membersCount = meetMemberRepository.findAllByMeet_Id(meetId)
                .stream()
                .map(mm -> mm.getMember())
                .count();

        return MeetDto.from(meet, addresses, member, 0L, membersCount);
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

    /**
     * 조건에 해당하는 모임을 조회하고 모임 DTO 목록을 반환한다.
     *
     * @param meetSearch 모임 목록 검색 조건(카테고리, 동네, 제목)이 담긴 DTO
     * @return 조회한 모임 DTO 목록
     */
    public List<MeetDto> findAllMeets(MeetSearch meetSearch) {
        // 카테고리 및 동네, 제목으로 조회한 모임 리스트
        List<Meet> meets = meetRepository.searchMeets(meetSearch);

        // 모임 리스트를 주소 및 리더 정보를 포함한 DTO 리스트로 변환해서 반환한다.
        List<MeetDto> meetDtos = new ArrayList<>();
        for (Meet meet : meets) {
            List<Address> addresses = meetAddressRepository.findAllByMeet_Id(meet.getId())
                    .stream()
                    .map(ma -> ma.getAddress())
                    .collect(Collectors.toUnmodifiableList());

            Member leader = memberRepository.findById(meet.getCreatedBy())
                    .orElseThrow(() -> new MemberIdNotFoundException(meet.getCreatedBy()));

            //TODO : 좋아요 수 추후 구현 필요. 모집글 목록조회이므로 인원 카운트는 필요 X
            meetDtos.add(MeetDto.from(meet, addresses, leader, 0L, 1L));
        }

        return meetDtos;
    }

    // 조건에 해당하는 모임 기록을 찾아서 모임 DTO 목록을 반환한다.
    public List<MeetDto> findAllMeetsRecords(MeetSearch meetSearch) {
        System.out.println("meetSearch = " + meetSearch);
        // 모임 진행상태로 조회한 모임 리스트
        List<Meet> meets = meetRepository.searchMeets(meetSearch);
        System.out.println("meets.size() = " + meets.size());

        // 모임 리스트를 주소 및 리더 정보, 모임 인원수를 포함한 DTO 리스트로 변환해서 반환한다.
        List<MeetDto> meetDtos = new ArrayList<>();
        for (Meet meet : meets) {
            List<Address> addresses = meetAddressRepository.findAllByMeet_Id(meet.getId())
                    .stream()
                    .map(ma -> ma.getAddress())
                    .collect(Collectors.toUnmodifiableList());

            Member leader = memberRepository.findById(meet.getCreatedBy())
                    .orElseThrow(() -> new MemberIdNotFoundException(meet.getCreatedBy()));

            long membersCount = meetMemberRepository.findAllByMeet_Id(meet.getId())
                    .stream()
                    .map(mm -> mm.getMember())
                    .count();

            // 모임 기록 조회이므로 좋아요 수는 필요 없다.
            meetDtos.add(MeetDto.from(meet, addresses, leader, 0L, membersCount));
        }

        return meetDtos;
    }
}