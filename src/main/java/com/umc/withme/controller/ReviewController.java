package com.umc.withme.controller;

import com.umc.withme.dto.common.DataResponse;
import com.umc.withme.dto.review.ReviewCreateRequest;
import com.umc.withme.dto.review.ReviewCreateResponse;
import com.umc.withme.security.WithMeAppPrinciple;
import com.umc.withme.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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

    @Operation(
            summary = "회원 후기 글 생성",
            description = "<p>request param과 request body로 받은 데이터를 사용해 회원에 대한 후기 글을 생성합니다.",
            security = @SecurityRequirement(name = "access-token")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "1402: <code>id</code>에 해당하는 회원이 없는 경우 \t\n 2400: <code>id</code>에 해당하는 모임이 없는 경우", content = @Content)
    })
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
}
