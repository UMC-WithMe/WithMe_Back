package com.umc.withme.service;

import com.umc.withme.domain.Meet;
import com.umc.withme.domain.Member;
import com.umc.withme.domain.Point;
import com.umc.withme.domain.Review;
import com.umc.withme.dto.review.ReviewCreateRequest;
import com.umc.withme.dto.review.ReviewDto;
import com.umc.withme.dto.review.ReviewInfoResponse;
import com.umc.withme.exception.meet.MeetIdNotFoundException;
import com.umc.withme.exception.member.MemberIdNotFoundException;
import com.umc.withme.repository.MeetRepository;
import com.umc.withme.repository.MemberRepository;
import com.umc.withme.repository.PointRepository;
import com.umc.withme.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * Point와 Review 엔티티에 필요한 값들을 입력받아 DB에 저장
     *
     * @param senderId
     * @param receiverId
     * @param meetId
     * @param requestDto
     * @return DB에 저장된 후기 아이디
     */
    @Transactional
    public Long create(Long senderId, Long receiverId, Long meetId, ReviewCreateRequest requestDto) {

        Member sender = memberRepository.findById(senderId).orElseThrow(()->new MemberIdNotFoundException(senderId));
        Member receiver = memberRepository.findById(receiverId).orElseThrow(()->new MemberIdNotFoundException(receiverId));
        Meet meet = meetRepository.findById(meetId).orElseThrow(()-> new MeetIdNotFoundException(meetId));

        Point point = pointRepository.save(requestDto.getPoint().toDto().toEntity());

        Review review = Review.builder()
                .sender(sender)
                .receiver(receiver)
                .meet(meet)
                .point(point)
                .content(requestDto.getContent())
                .build();

        return reviewRepository.save(review).getId();
    }

    /**
     * 로그인된 사용자의 아이디(PK)로 받은 리뷰들을 조회해 필요한 데이터를 리스트에 담아 반환
     *
     * @param id 로그인한 사용자의 아이디
     * @return ReviewGetInfoResponse 타입의 리스트
     */
    public List<ReviewInfoResponse> getReceiveReview(Long loginMemberId) {
        return reviewRepository.findAllByReceiver_Id(loginMemberId).stream()
                .map(ReviewDto::from)
                .map(ReviewInfoResponse::from)
                .collect(Collectors.toUnmodifiableList());
    }

    /**
     * 받은 리뷰 개수 조회
     * @param memberId 본인(받은 사람) 회원 id(pk)
     * @return 받은 리뷰 개수
     */
    public Long getReceivedReviewsCount(Long memberId){
        return reviewRepository.countByReceiver_Id(memberId);
    }
}
