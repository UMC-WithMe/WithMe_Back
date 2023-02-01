package com.umc.withme.dto.review;

import com.umc.withme.dto.meet.MeetDto;
import com.umc.withme.dto.member.MemberDto;
import com.umc.withme.dto.point.PointDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ReviewDto {

    private Long reviewId;
    private MemberDto sender;
    private MemberDto receiver;
    private MeetDto meet;
    private PointDto point;
    private String content;

    public static ReviewDto of(Long reviewId, MemberDto sender, MemberDto receiver, MeetDto meet, PointDto point, String content) {
        return new ReviewDto(reviewId, sender, receiver, meet, point, content);
    }

    public static ReviewDto of(MemberDto sender, MemberDto receiver, MeetDto meet, PointDto point, String content) {
        return ReviewDto.of(null, sender, receiver, meet, point, content);
    }
}
