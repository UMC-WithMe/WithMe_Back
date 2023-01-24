package com.umc.withme.controller;

import com.umc.withme.dto.common.DataResponse;
import com.umc.withme.dto.member.MemberDto;
import com.umc.withme.dto.member.MemberInfoGetResponse;
import com.umc.withme.dto.member.NicknameDuplicationCheckResponse;
import com.umc.withme.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.ConstraintViolationException;
import javax.validation.constraints.NotBlank;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Validated
public class MemberController {

    private final MemberService memberService;

    /**
     * 닉네임 중복 조회를 하는 API
     *
     * @param nickname
     * @return 중복 여부를 {@link NicknameDuplicationCheckResponse}에 담아 {@link DataResponse}로 반환
     * @throws ConstraintViolationException 닉네임이 공백인 경우
     */
    // TODO: Swagger 문서에 적용
    @GetMapping("/check/duplicate")
    public ResponseEntity<DataResponse<NicknameDuplicationCheckResponse>> checkNicknameDuplicate(@RequestParam @NotBlank String nickname) {
        NicknameDuplicationCheckResponse response = NicknameDuplicationCheckResponse.of(memberService.checkNicknameDuplication(nickname));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new DataResponse<>(response));
    }

    /**
     * 특정 닉네임을 가지는 회원 정보를 조회하는 API
     *
     * @param nickname
     * @return {@link MemberInfoGetResponse}에 회원 정보를 담아 {@link DataResponse}로 반환
     */
    @GetMapping("/users")
    public ResponseEntity<DataResponse<MemberInfoGetResponse>> getMemberInfo(@RequestParam @NotBlank String nickname) {
        MemberDto memberDto = memberService.findMemberByNickname(nickname);
        MemberInfoGetResponse response = MemberInfoGetResponse.from(memberDto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new DataResponse<>(response));
    }
}
