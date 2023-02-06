package com.umc.withme.controller;

import com.umc.withme.dto.common.BaseResponse;
import com.umc.withme.dto.like.MeetLikeCreateRequest;
import com.umc.withme.dto.message.MessageCreateResponse;
import com.umc.withme.security.WithMeAppPrinciple;
import com.umc.withme.service.MeetLikeService;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "모집글 찜 API", description = "모집글 찜 관련 api 입니다.")
public class MeetLikeController {

    private final MeetLikeService meetLikeService;

    @Operation(summary = "찜 생성",
            description = "<p> 회원이 찜 1개를 생성합니다:: <code>meetLikeCreateRequest</code>의 <code>meetId</code></p> - 찜하는 모집글 id(pk)",
            security = @SecurityRequirement(name = "access-token"))
    @PostMapping("/meet-likes")
    public ResponseEntity<BaseResponse> createMeetLike(
            @Parameter(hidden = true) @AuthenticationPrincipal WithMeAppPrinciple principle,
            @Valid @RequestBody MeetLikeCreateRequest meetLikeCreateRequest){

        meetLikeService.createMeetLike(principle.getMemberId(), meetLikeCreateRequest.getMeetId());

        return ResponseEntity.status(HttpStatus.CREATED).body(new BaseResponse(true));
    }
}