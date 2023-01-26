package com.umc.withme.controller;

import com.umc.withme.dto.common.DataResponse;
import com.umc.withme.dto.review.ReviewCreateRequest;
import com.umc.withme.dto.review.ReviewCreateResponse;
import com.umc.withme.security.WithMeAppPrinciple;
import com.umc.withme.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/review")
    public ResponseEntity<DataResponse<ReviewCreateResponse>> createReview(
            @Valid @RequestBody ReviewCreateRequest reviewCreateRequest,
            @RequestParam Long receiverId,
            @RequestParam Long meetId,
            @AuthenticationPrincipal WithMeAppPrinciple principle
    ) {
        Long reviewId = reviewService.create(principle.getMemberId(), receiverId, meetId, reviewCreateRequest);

        ReviewCreateResponse response = ReviewCreateResponse.of(reviewId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new DataResponse<>(response));
    }
}
