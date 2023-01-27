package com.umc.withme.dto.point;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class PointRequest {

    @Schema(example = "80", description = "모임 회원의 출석률")
    @NotNull
    @Min(value = 0) @Max(value = 100)
    private Integer attendance;

    @Schema(example = "70", description = "모임 회원의 열정")
    @NotNull
    @Min(value = 0) @Max(value = 100)
    private Integer passion;

    @Schema(example = "100", description = "모임 회원의 연락속도")
    @NotNull
    @Min(value = 0) @Max(value = 100)
    private Integer contactSpeed;

    public PointDto toDto() {
        return PointDto.of(null, attendance, passion, contactSpeed);
    }
}
