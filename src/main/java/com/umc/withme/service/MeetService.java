package com.umc.withme.service;

import com.umc.withme.domain.*;
import com.umc.withme.domain.constant.MeetCategory;
import com.umc.withme.domain.constant.MeetStatus;
import com.umc.withme.dto.address.AddressDto;
import com.umc.withme.dto.meet.MeetDto;
import com.umc.withme.dto.meet.MeetRecordSearch;
import com.umc.withme.dto.meet.MeetSearch;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MeetService {

    private final S3FileService s3FileService;
    private final MeetRepository meetRepository;
    private final MeetAddressRepository meetAddressRepository;
    private final MeetMemberRepository meetMemberRepository;
    private final AddressRepository addressRepository;
    private final MemberRepository memberRepository;

    /**
     * MeetDto와 leaderName 입력받아 MeetRepository 및 MeetAddressRepository에 저장한다.
     *
     * @param meetDto     생성하고자 하는 모임 모집글 정보
     * @param meetImage
     * @param leaderEmail 생성하고자 하는 모임 모집글의 글쓴이 이메일 (현재 로그인한 사용자의 이메일)
     * @return 생성한 모임의 id
     */
    @Transactional
    public Long createMeet(MeetDto meetDto, MultipartFile meetImage, String leaderEmail) {
        Member leader = getMemberByEmail(leaderEmail);
        ImageFile meetImageFile = s3FileService.saveFile(meetImage);

        Meet meet = meetRepository.save(meetDto.toEntity(meetImageFile));
        meetMemberRepository.save(new MeetMember(leader, meet));

        meetDto.getAddresses().forEach(addressDto -> meetAddressRepository.save(
                new MeetAddress(meet, this.convertAddressDtoToAddress(addressDto))
        ));

        return meet.getId();
    }

    /**
     * 모임 id를 입력받아 모임 DTO를 컨트롤러에게 반환하는 함수이다.
     *
     * @param meetId 조회할 모임의 id
     * @return 조회된 모임의 DTO
     */
    public MeetDto findById(Long meetId) {
        Meet meet = getMeetById(meetId);

        return createMeetDtoForResponse(meet);
    }

    /**
     * 조건에 해당하는 모임 모집글을 조회하고 모임 DTO 목록을 반환한다.
     *
     * @param category 모임 카테고리 조건 (모든 카테고리일 경우 null)
     * @param isLocal  내동네이면 ture, 온동네이면 false
     * @param title    모임 제목 검색 조건
     * @param memberId 현재 로그인한 사용자 id
     * @return 조회한 모임 DTO 목록
     */
    public List<MeetDto> findAllMeets(MeetCategory category, Boolean isLocal, String title, Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberIdNotFoundException(memberId));

        // 검색 조건 설정
        MeetSearch meetSearch;
        if (isLocal && member.getAddress() != null) {
            meetSearch = MeetSearch.of(category, member.getAddress().getSido(), member.getAddress().getSgg(), title);
        } else {
            meetSearch = MeetSearch.of(category, null, null, title);
        }

        // 카테고리 및 동네, 제목으로 조회한 모임 리스트
        List<Meet> meets = meetRepository.searchMeets(meetSearch);

        return getMeetDtosFromMeets(meets);
    }

    /**
     * 조건에 해당하는 모임 기록을 조회하고 모임 DTO 목록을 반환한다.
     *
     * @param meetRecordSearch 모임 목록 검색 조건(모임 진행상태, 사용자 id)이 담긴 DTO
     * @return 조회한 모임 DTO 목록
     */
    public List<MeetDto> findAllMeetsRecords(MeetRecordSearch meetRecordSearch) {
        // 모임 진행상태로 조회한 모임 리스트
        List<Meet> meets = meetRepository.searchMeetRecords(meetRecordSearch);

        return getMeetDtosFromMeets(meets);
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
    public MeetDto updateMeetById(Long meetId, Long loginMemberId, MeetDto meetDtoToUpdate, MultipartFile meetImage) {
        Meet meet = getMeetById(meetId);
        Member meetLeader = getMemberById(meet.getCreatedBy());

        validateUpdateAuthority(loginMemberId, meetLeader.getId());

        if (meetImage != null) {
            s3FileService.deleteFile(meet.getMeetImage());

            ImageFile newMeetImage = s3FileService.saveFile(meetImage);
            meet.setMeetImage(newMeetImage);
        }

        // 모임의 MeetAddress 모두 삭제 후 meetDtoToUpdate에 따라 다시 생성
        meetAddressRepository.deleteAllByMeet_Id(meet.getId());
        meetDtoToUpdate.getAddresses().forEach(
                addressDto -> meetAddressRepository.save(new MeetAddress(meet, convertAddressDtoToAddress(addressDto)))
        );

        // 모임 엔티티 수정
        meet.update(meetDtoToUpdate);

        // 수정된 모임 정보 바탕으로 MeetDto 생성해서 반환
        return MeetDto.from(
                meet,
                getAddressesByMeetId(meetId),
                meetLeader
        );
    }

    /**
     * 사용자가 모임의 리더인 경우, 모임의 상태가 완료 상태로 변경되고 변경된 모임 DTO를 반환한다.
     * 사용자가 모임의 리더가 아닌 경우, 예외가 발생한다.
     *
     * @param meetId        해제하려는 모임 id
     * @param loginMemberId 현재 로그인한 사용자 id
     * @return 모임의 상태가 완료로 변경된 모임 DTO
     */
    @Transactional
    public MeetDto setMeetComplete(Long meetId, Long loginMemberId) {
        Meet meet = getMeetById(meetId);

        validateUpdateAuthority(loginMemberId, meet.getCreatedBy());

        // 모임의 상태를 완료 상태로 변경
        meet.setMeetStatus(MeetStatus.COMPLETE);

        return createMeetDtoForResponse(meet);
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
        Meet meet = getMeetById(meetId);

        validateMeetDeleteAuthority(loginMemberId, meet.getCreatedBy());

        meetAddressRepository.deleteAllByMeet_Id(meetId);
        meetMemberRepository.deleteAllByMeet_Id(meetId);
        meetRepository.delete(meet);
    }

    /**
     * email과 일치하는 회원 조회
     *
     * @param email 조회할 회원의 email
     * @return 조회된 {@link Member} entity
     * @throws EmailNotFoundException email에 해당하는 회원이 없는 경우
     */
    private Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(EmailNotFoundException::new);
    }

    /**
     * memberId와 일치하는 회원 조회
     *
     * @param memberId 조회할 회원의 id(PK)
     * @return 조회된 {@link Member} entity
     * @throws MemberIdNotFoundException memberId에 해당하는 회원이 없는 경우
     */
    private Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberIdNotFoundException(memberId));
    }

    /**
     * meetId와 일치하는 모임 조회
     *
     * @param meetId 조회할 모임의 id(PK)
     * @return 조회된 {@link Meet} entity
     * @throws MeetIdNotFoundException meetId에 해당하는 회원이 없는 경우
     */
    private Meet getMeetById(Long meetId) {
        return meetRepository.findById(meetId)
                .orElseThrow(() -> new MeetIdNotFoundException(meetId));
    }

    /**
     * meetId에 해당하는 모임에 등록된 주소 entity를 전부 조회한다.
     *
     * @param meetId 주소 목록을 불러올 모임의 id(PK)
     * @return 조회된 {@link Address} 리스트
     */
    private List<Address> getAddressesByMeetId(Long meetId) {
        return meetAddressRepository.findAllByMeet_Id(meetId)
                .stream()
                .map(MeetAddress::getAddress)
                .collect(Collectors.toUnmodifiableList());
    }

    /**
     * AddressDto를 Address Entity로 변환해 반환해준다.
     *
     * @param dto 변환할 AddressDto
     * @return 변환된 Address Entity
     */
    private Address convertAddressDtoToAddress(AddressDto dto) {
        return addressRepository.findBySidoAndSgg(dto.getSido(), dto.getSgg())
                .orElseThrow(() -> new AddressNotFoundException(dto.getSido(), dto.getSgg()));
    }

    /**
     * 모임 리스트를 주소 및 리더 정보를 포함한 DTO 리스트로 변환해서 반환한다.
     *
     * @param meets
     * @return
     */
    private List<MeetDto> getMeetDtosFromMeets(List<Meet> meets) {
        List<MeetDto> meetDtos = new ArrayList<>();

        //TODO : 모임 인원 수 및 좋아요 수 추후 구현 필요
        meets.forEach(meet -> {
            List<Address> addresses = getAddressesByMeetId(meet.getId());

            Member leader = memberRepository.findById(meet.getCreatedBy())
                    .orElseThrow(() -> new MemberIdNotFoundException(meet.getCreatedBy()));

            meetDtos.add(MeetDto.from(meet, addresses, leader));
        });

        return meetDtos;
    }

    /**
     * Meet entity를 사용하여 API 응답을 위한 MeetDto롤 생성한다.
     *
     * @param meet 응답할 데이터를 담고 있는 모임 entity
     * @return meet를 사용하여 생성한 {@link MeetDto}
     */
    private MeetDto createMeetDtoForResponse(Meet meet) {
        return MeetDto.from(
                meet,
                getAddressesByMeetId(meet.getId()),
                getMemberById(meet.getCreatedBy())
        );
    }

    /**
     * 로그인 회원이 모임/모집글에 대한 수정 권한이 있는지 검증한다.
     *
     * @param loginMemberId 로그인 회원
     * @param meetLeaderId  수정하고자 하는 모임/모집글
     * @throws MeetUpdateForbiddenException 수정 권한이 없는 경우. 즉, 로그인 회원과 모임의 리더가 다른 경우.
     */
    private void validateUpdateAuthority(Long loginMemberId, Long meetLeaderId) {
        if (!loginMemberId.equals(meetLeaderId)) {
            throw new MeetUpdateForbiddenException();
        }
    }

    /**
     * 로그인 회원이 모임/모집글에 대한 삭제 권한이 있는지 검증한다.
     *
     * @param loginMemberId 로그인 회원
     * @param meetLeaderId  삭제하고자 하는 모임/모집글
     * @throws MeetUpdateForbiddenException 삭제 권한이 없는 경우. 즉, 로그인 회원과 모임의 리더가 다른 경우.
     */
    private void validateMeetDeleteAuthority(Long loginMemberId, Long meetLeaderId) {
        if (!meetLeaderId.equals(loginMemberId)) {
            throw new MeetDeleteForbiddenException();
        }
    }
}