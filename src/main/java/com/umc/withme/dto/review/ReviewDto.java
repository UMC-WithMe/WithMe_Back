package com.umc.withme.dto.review;

import com.umc.withme.domain.Review;
import com.umc.withme.dto.meet.MeetDto;
import com.umc.withme.dto.member.MemberDto;
import com.umc.withme.dto.point.PointDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ReviewDto {

    private Long reviewId;
    private MemberDto sender;
    private MemberDto receiver;
    private MeetDto meet;
    private PointDto point;
    private String content;
    private LocalDateTime createdAt;

    public static ReviewDto from(Review review) {
        return new ReviewDto(
                review.getId(),
                MemberDto.from(review.getSender()),
                MemberDto.from(review.getReceiver()),
                MeetDto.from(review.getMeet()),
                PointDto.from(review.getPoint()),
                review.getContent(),
                review.getCreatedAt()
        );
    }
}
