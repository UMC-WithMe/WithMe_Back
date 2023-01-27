package com.umc.withme.dto.review;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ReviewCreateResponse {

    @Schema(example = "5", description = "작성 완료된 후기 아이디")
    private Long reviewId;

    public static ReviewCreateResponse of(Long reviewId){
        return new ReviewCreateResponse(reviewId);
    }
}
