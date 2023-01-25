package com.umc.withme.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemberPhoneNumberUpdateRequest {

    @Schema(example = "010-1234-5678", description = "설정할 폰 번호")
    @NotBlank
    @Pattern(regexp = "[0-9]{3}-+[0-9]{4}-+[0-9]{4}", message = "전화번호는 010-XXXX-XXXX 형태로 전달해주세요.")
    private String phoneNumber;
}
