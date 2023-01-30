package com.umc.withme.service;

import com.umc.withme.domain.*;
import com.umc.withme.dto.address.AddressDto;
import com.umc.withme.dto.meet.MeetDto;
import com.umc.withme.dto.meet.MeetSearch;
import com.umc.withme.exception.address.AddressNotFoundException;
import com.umc.withme.exception.common.NotFoundException;
import com.umc.withme.exception.common.UnauthorizedException;
import com.umc.withme.exception.meet.MeetIdNotFoundException;
import com.umc.withme.exception.member.EmailNotFoundException;
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

        //TODO : 좋아요 수 및 인원수 설정
        return MeetDto.from(meet, addresses, member, 0L, 1L);
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
                    .orElseThrow(NotFoundException::new);   // TODO : 모임수정브랜치 merge 되면 exception 변경 필요
            //TODO : 좋아요 수 및 인원수 설정
            meetDtos.add(MeetDto.from(meet, addresses, leader, 0L, 1L));
        }

        // TODO : 하트 갯수 넣기

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
            System.out.println("set addresses");
            Member leader = memberRepository.findById(meet.getCreatedBy())
                    .orElseThrow(NotFoundException::new);   // TODO : 모임수정브랜치 merge 되면 exception 변경 필요
            System.out.println("set leader");
            long membersCount = meetMemberRepository.findAllByMeet_Id(meet.getId())
                    .stream()
                    .map(mm -> mm.getMember())
                    .count();
            System.out.println("set membersCount");

            meetDtos.add(MeetDto.from(meet, addresses, leader, 0L, membersCount));
        }

        return meetDtos;
    }
}