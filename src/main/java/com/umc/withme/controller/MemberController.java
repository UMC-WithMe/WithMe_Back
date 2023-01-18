package com.umc.withme.controller;

import com.umc.withme.dto.common.DataResponse;
import com.umc.withme.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
     * @return 중복 여부를 DataResponse에 담아 반환
     * @throws ConstraintViolationException 닉네임이 공백인 경우
     */
    // TODO: Swagger 문서에 적용
    @GetMapping("/check/duplicate")
    public ResponseEntity<DataResponse> checkNicknameDuplicate(
            @RequestParam @NotBlank String nickname
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new DataResponse(memberService.checkNicknameDuplication(nickname)));
    }
}
