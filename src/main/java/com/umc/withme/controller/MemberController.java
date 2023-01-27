package com.umc.withme.controller;

import com.umc.withme.dto.common.BaseResponse;
import com.umc.withme.dto.common.DataResponse;
import com.umc.withme.dto.member.*;
import com.umc.withme.security.WithMeAppPrinciple;
import com.umc.withme.service.MemberService;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Validated
public class MemberController {

    private final MemberService memberService;

    @Operation(
            summary = "닉네임 중복 조회",
            description = "<p>해당 <code>nickname</code>이 다른 사용자가 사용 중인지 확인한다. " +
                    "반환 값이 true이면 이미 사용 중인 닉네임이고, false이면 사용 중이지 않는 닉네임이다.</p>",
            security = @SecurityRequirement(name = "access-token")
    )
    @GetMapping("/members/check")
    public ResponseEntity<DataResponse<NicknameDuplicationCheckResponse>> checkNicknameDuplicate(@RequestParam @NotBlank String nickname) {
        NicknameDuplicationCheckResponse response = NicknameDuplicationCheckResponse.of(memberService.checkNicknameDuplication(nickname));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new DataResponse<>(response));
    }

    @Operation(
            summary = "특정 닉네임을 가지는 회원 정보 조회",
            description = "<p>해당 <code>nickname</code>을 가지는 사용자의 정보를 조회한다.</p>",
            security = @SecurityRequirement(name = "access-token")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "404", description = "1400: <code>nickname</code>을 가지는 회원이 없는 경우", content = @Content)
    })
    @GetMapping("/members")
    public ResponseEntity<DataResponse<MemberInfoGetResponse>> getMemberInfo(@RequestParam @NotBlank String nickname) {
        MemberDto memberDto = memberService.findMemberByNickname(nickname);
        MemberInfoGetResponse response = MemberInfoGetResponse.from(memberDto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new DataResponse<>(response));
    }

    @Operation(
            summary = "회원 폰 번호 설정/재설정",
            description = "<p><code>memberId</code>에 해당하는 회원의 폰 번호를 request body의 <code>phoneNumber</code>로 설정합니다.</p>",
            security = @SecurityRequirement(name = "access-token")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "404", description = "1401: <code>email</code>에 해당하는 회원이 없는 경우", content = @Content)
    })
    @PatchMapping("/user/phone-number")
    public ResponseEntity<BaseResponse> updateMemberPhoneNumber(
            @Parameter(hidden = true) @AuthenticationPrincipal WithMeAppPrinciple principle,
            @Valid @RequestBody MemberPhoneNumberUpdateRequest request
    ) {
        memberService.updateMemberPhoneNumber(principle.getUsername(), request.getPhoneNumber());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse(true));
    }

    @Operation(
            summary = "회원 주소 설정/재설정",
            description = "<p><code>memberId</code>에 해당하는 회원의 주소 정보를 request body의 <code>sido</code>, <code>sgg</code>로 설정합니다.</p>",
            security = @SecurityRequirement(name = "access-token")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "404", description = "1401: <code>email</code>에 해당하는 회원이 없는 경우", content = @Content)
    })
    @PatchMapping("/user/address")
    public ResponseEntity<BaseResponse> updateMemberAddress(
            @Parameter(hidden = true) @AuthenticationPrincipal WithMeAppPrinciple principle,
            @Valid @RequestBody MemberAddressUpdateRequest request
    ) {
        memberService.updateMemberAddress(principle.getUsername(), request.getSido(), request.getSgg());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse(true));
    }
}
