package com.umc.withme.dto.member.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemberNicknameUpdateRequest {

    // TODO: 추후 닉네임 최대/최소 글자수 반영 필요
    @NotBlank
    private String nickname;
}
