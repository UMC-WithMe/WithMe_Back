package com.umc.withme.service;

import com.umc.withme.domain.*;
import com.umc.withme.dto.review.*;
import com.umc.withme.exception.meet.MeetIdNotFoundException;
import com.umc.withme.exception.member.MemberIdNotFoundException;
import com.umc.withme.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final MeetRepository meetRepository;
    private final PointRepository pointRepository;
    private final MeetMemberRepository meetMemberRepository;

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

        Member sender = memberRepository.findById(senderId).orElseThrow(MemberIdNotFoundException::new);
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
     * 사용자의 아이디(PK)를 입력받아 참여했던 모든 모임을 조회한 뒤 정렬해 가장 최근에 끝난 모임 최대 2개를 가져온다.
     * 모임의 정보와 사용자의 아이디로 받은 후기들을 조회해 반환
     *
     * @param memberId 사용자의 아이디(PK)
     * @return RecentReviewInfoResponse
     */
    public RecentReviewInfoResponse getRecentTwoMeetReview(Long memberId) {

        List<Meet> twoMeetList = new ArrayList<>();
        List<List<ReviewDto>> reviewList = new ArrayList<>();

        // 사용자가 참여한 모든 모임 조회
        List<Meet> meetList = meetMemberRepository.findAllByMember_Id(memberId).stream()
                                                    .map(mm -> meetRepository.findById(mm.getMeet().getId()).orElseThrow(MeetIdNotFoundException::new))
                                                    .sorted(Comparator.comparing(Meet::getEndDate).reversed())
                                                    .collect(Collectors.toUnmodifiableList());

        // 가장 최근 끝난 모임 2개 가져오기
        if (meetList.size() > 1) {
            twoMeetList = meetList.subList(0, 2);
        } else if (meetList.size() == 1) {
            Collections.copy(twoMeetList, meetList);
        } else {
            return (RecentReviewInfoResponse) Collections.emptyMap();
        }

        // 가져온 모임에서 받은 후기 조회
        twoMeetList.forEach(m -> {
            List<ReviewDto> collect = reviewRepository.findAllByMeet_IdAndReceiver_Id(m.getId(), memberId).stream()
                    .map(ReviewDto::from)
                    .collect(Collectors.toUnmodifiableList());
            reviewList.add(collect);
        });

        return RecentReviewInfoResponse.from(reviewList.get(0), reviewList.get(1));
    }
}
