package com.umc.withme.dto.review;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ReviewCreateResponse {

    private Long reviewId;

    public static ReviewCreateResponse of(Long reviewId){
        return new ReviewCreateResponse(reviewId);
    }
}
