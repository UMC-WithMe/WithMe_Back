package com.umc.withme.controller;

import com.umc.withme.dto.common.DataResponse;
import com.umc.withme.dto.review.*;
import com.umc.withme.security.WithMeAppPrinciple;
import com.umc.withme.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "후기 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(
            summary = "회원 후기 글 생성",
            description = "<p>request param과 request body로 받은 데이터를 사용해 회원에 대한 후기 글을 생성합니다.",
            security = @SecurityRequirement(name = "access-token")
    )
    @PostMapping("/review")
    public ResponseEntity<DataResponse<ReviewCreateResponse>> createReview(
            @Valid @RequestBody ReviewCreateRequest reviewCreateRequest,
            @RequestParam Long receiverId,
            @RequestParam Long meetId,
            @Parameter(hidden = true) @AuthenticationPrincipal WithMeAppPrinciple principle
    ) {
        Long reviewId = reviewService.create(principle.getMemberId(), receiverId, meetId, reviewCreateRequest);

        ReviewCreateResponse response = ReviewCreateResponse.of(reviewId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new DataResponse<>(response));
    }

    @Operation(
            summary = "받은 후기 조회",
            description = "로그인된 사용자의 아이디(pk)로 받은 후기를 조회합니다.",
            security = @SecurityRequirement(name = "access-token")
    )
    @GetMapping("/reviews/receive-reviews")
    public ResponseEntity<DataResponse<List<ReviewInfoResponse>>> getReceiveReviews(
            @Parameter(hidden = true) @AuthenticationPrincipal WithMeAppPrinciple principle
    ) {
        List<ReviewInfoResponse> response = reviewService.getReceiveReviews(principle.getMemberId());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new DataResponse<>(response));
    }

    @Operation(
            summary = "작성한 후기 글 조회",
            description = "로그인된 사용자의 아이디(pk)로 작성한 후기를 조회합니다.",
            security = @SecurityRequirement(name = "access-token")
    )
    @GetMapping("/reviews/send-reviews")
    public ResponseEntity<DataResponse<List<ReviewInfoResponse>>> getSendReviews(
            @Parameter(hidden = true) @AuthenticationPrincipal WithMeAppPrinciple principle
    ) {
        List<ReviewInfoResponse> response = reviewService.getSendReviews(principle.getMemberId());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new DataResponse<>(response));
    }

    @Operation(
            summary = "최근에 받은 후기 조회",
            description = "사용자의 아이디(PK)를 입력받아 최근에 끝난 2개 모임에서 받은 후기를 조회합니다.",
            security = @SecurityRequirement(name = "access-token")
    )
    @GetMapping("/reviews/recent-receive-reviews/{memberId}")
    public ResponseEntity<DataResponse<RecentReviewInfoResponse>> getRecentReviews(@PathVariable Long memberId) {
        RecentReviewInfoResponse response = reviewService.getRecentTwoMeetReview(memberId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new DataResponse<>(response));
    }
}
