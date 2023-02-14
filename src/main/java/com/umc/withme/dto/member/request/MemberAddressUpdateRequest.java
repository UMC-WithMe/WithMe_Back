package com.umc.withme.dto.member.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemberAddressUpdateRequest {

    @Schema(example = "서울특별시", description = "시/도")
    @NotBlank
    private String sido;

    @Schema(example = "강남구", description = "시/군/구")
    @NotBlank
    private String sgg;
}
