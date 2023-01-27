package com.umc.withme.service;

import com.umc.withme.domain.*;
import com.umc.withme.dto.review.ReviewCreateRequest;
import com.umc.withme.exception.common.UnauthorizedException;
import com.umc.withme.exception.meet.MeetIdNotFoundException;
import com.umc.withme.exception.member.MemberIdNotFoundException;
import com.umc.withme.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


        Member sender = memberRepository.findById(senderId).orElseThrow(UnauthorizedException::new);
        Member receiver = memberRepository.findById(receiverId).orElseThrow(MemberIdNotFoundException::new);
        Meet meet = meetRepository.findById(meetId).orElseThrow(MeetIdNotFoundException::new);

        Point point = pointRepository.save(requestDto.getPoint().toDto().toEntity());

        Review review = Review.builder()
                .sender(sender)
                .receiver(receiver)
                .meet(meet)
                .point(point)
                .content(requestDto.getContent())
                .build();

        Review saveReview = reviewRepository.save(review);

        return saveReview.getId();
    }
}