package com.umc.withme.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.umc.withme.dto.auth.KakaoOAuthResponse;
import com.umc.withme.dto.auth.LoginRequest;
import com.umc.withme.dto.auth.LoginResponse;
import com.umc.withme.dto.common.DataResponse;
import com.umc.withme.security.WithMeAppPrinciple;
import com.umc.withme.service.AuthService;
import com.umc.withme.service.MemberService;
import io.swagger.v3.oas.annotations.Hidden;
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

@Tag(name = "Login & SignUp")
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@RestController
public class AuthController {

    private final AuthService authService;
    private final MemberService memberService;
    private final KakaoOAuthController kakaoOAuthController;

    @Operation(
            summary = "로그인",
            description = "<p>Kakao에서 전달받은 access token(<code>kakaoAccessToken</code>)으로 server에 로그인합니다.</p>" +
                    "<p>로그인에 성공하면 jwt access token을 응답합니다.</p>"
    )
    @PostMapping("/login")
    public ResponseEntity<DataResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) throws JsonProcessingException {
        KakaoOAuthResponse userInfo = kakaoOAuthController.getUserInfo(request.getKakaoAccessToken());

        String email = userInfo.getEmail();
        if (!memberService.existsMemberByEmail(email)) {
            authService.signUp(userInfo.toMemberDto());
        }

        String jwtToken = authService.login(email);
        boolean phoneNumberExistence = memberService.checkExistenceMemberPhoneNumberByEmail(email);
        boolean addressExistence = memberService.checkExistenceMemberAddressByEmail(email);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new DataResponse<>(
                        LoginResponse.of(
                                phoneNumberExistence,
                                addressExistence,
                                jwtToken
                        ))
                );
    }

    @Hidden
    @Operation(
            summary = "개발용 테스트 API (사용하지 말 것)",
            security = @SecurityRequirement(name = "access-token")
    )
    @GetMapping("/check")
    public ResponseEntity<DataResponse<WithMeAppPrinciple>> check(
            @Parameter(hidden = true) @AuthenticationPrincipal WithMeAppPrinciple principle
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new DataResponse<>(principle));
    }
}
