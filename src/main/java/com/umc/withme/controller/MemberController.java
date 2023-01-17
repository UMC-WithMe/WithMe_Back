package com.umc.withme.controller;

import com.umc.withme.dto.common.BaseResponse;
import com.umc.withme.dto.common.DataResponse;
import com.umc.withme.dto.common.ErrorResponse;
import com.umc.withme.exception.ExceptionType;
import com.umc.withme.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {

    private final MemberService memberService;

    /**
     * 닉네임 중복 조회를 하는 API
     *
     * @param nickname
     * @return 중복 여부를 포함한 DataResponse
     * @throws 닉네임 값이 공백일 때 ErrorResponse
     */
    // TODO: Swagger 문서에 적용
    @GetMapping("/check/{nickname}/exists")
    public BaseResponse checkNicknameDuplicate(@PathVariable String nickname) {
        if (nickname.isBlank()) {
            ExceptionType error = ExceptionType.MEMBER_NICKNAME_IS_NULL_EXCEPTION;
            return new ErrorResponse(error.getErrorCode(), error.getErrorMessage());
        }

        return new DataResponse(memberService.checkNicknameDuplication(nickname));
    }
}
