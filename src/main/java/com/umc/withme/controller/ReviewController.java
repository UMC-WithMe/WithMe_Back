package com.umc.withme.controller;

import com.umc.withme.dto.common.DataResponse;
import com.umc.withme.dto.review.ReviewCreateRequest;
import com.umc.withme.dto.review.ReviewCreateResponse;
import com.umc.withme.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * 모임이 끝난 후 참여한 회원에 대한 리뷰 작성 API
     *
     * @param reviewCreateRequest 별점 3개, 리뷰 내용
     * @param senderId 리뷰를 작성하는 회원 아이디
     * @param receiverId 리뷰 작성 대상이 되는 회원 아이디
     * @param meetId 끝난 모임 아이디
     * @return Long reviewId
     */
    @PostMapping("/review")
    public ResponseEntity<DataResponse<ReviewCreateResponse>> createReview(
            @Valid @RequestBody ReviewCreateRequest reviewCreateRequest,
            @RequestParam Long senderId, // TODO: 로그인한 사용자 정보로 변경
            @RequestParam Long receiverId,
            @RequestParam Long meetId
    ) {
        Long reviewId = reviewService.create(senderId, receiverId, meetId, reviewCreateRequest);

        ReviewCreateResponse response = ReviewCreateResponse.of(reviewId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new DataResponse<>(response));
    }
}
