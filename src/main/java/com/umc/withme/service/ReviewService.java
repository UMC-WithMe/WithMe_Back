package com.umc.withme.service;

import com.umc.withme.domain.*;
import com.umc.withme.dto.address.AddressDto;
import com.umc.withme.dto.meet.MeetDto;
import com.umc.withme.dto.member.MemberDto;
import com.umc.withme.dto.point.PointDto;
import com.umc.withme.dto.review.ReviewCreateRequest;
import com.umc.withme.dto.review.ReviewDto;
import com.umc.withme.exception.address.AddressNotFoundException;
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
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final MeetRepository meetRepository;
    private final PointRepository pointRepository;
    private final MeetAddressRepository meetAddressRepository;
    private final AddressRepository addressRepository;

    /**
     * 입력 받은 값을 회원, 모임, 별점 DTO로 변환하고 여기에 리뷰 내용을 가지는 ReviewDto를 만든다.
     * 만든 PointDto와 ReviewDto를 엔티티로 바꾼 후 Point와 Review 테이블에 저장한다.
     *
     * @param senderId
     * @param receiverId
     * @param meetId
     * @param requestDto
     * @return 작성된 리뷰 글의 아이디
     */
    @Transactional
    public Long create(Long senderId, Long receiverId, Long meetId, ReviewCreateRequest requestDto) {

        MemberDto senderDto = memberRepository.findById(senderId) // TODO: 로그인한 사용자 정보로 변경
                                                .map(MemberDto::from)
                                                .orElseThrow(EntityNotFoundException::new);

        MemberDto receiverDto = memberRepository.findById(receiverId)
                                                .map(MemberDto::from)
                                                .orElseThrow(EntityNotFoundException::new);

        Meet meet = meetRepository.findById(meetId).orElseThrow(EntityNotFoundException::new);

        List<Address> addresses = meetAddressRepository.findByMeet_Id(meetId)
                .stream()
                .map(ma -> ma.getAddress())
                .collect(Collectors.toUnmodifiableList());

        MeetDto meetDto = MeetDto.from(meet, addresses);

        PointDto pointDto = requestDto.getPoint().toDto();

        ReviewDto reviewDto = ReviewDto.of(senderDto, receiverDto, meetDto, pointDto, requestDto.getContent());

        Point point = pointRepository.save(pointDto.toEntity());

        Review review = reviewRepository.save(reviewDto.toEntity(senderDto.toEntity(), receiverDto.toEntity(), meet, point));

        return review.getId();
    }

    private Address getAddress(AddressDto dto) {
        return addressRepository.findBySidoAndSgg(dto.getSido(), dto.getSgg())
                .orElseThrow(() -> new AddressNotFoundException(dto.getSido(), dto.getSgg()));
    }
}
