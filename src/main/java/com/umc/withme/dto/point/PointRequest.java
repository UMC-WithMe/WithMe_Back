package com.umc.withme.dto.point;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class PointRequest {

    @NotNull
    @Min(value = 0) @Max(value = 100)
    private Integer attendance;

    @NotNull
    @Min(value = 0) @Max(value = 100)
    private Integer passion;

    @NotNull
    @Min(value = 0) @Max(value = 100)
    private Integer contactSpeed;

    public PointDto toDto() {
        return PointDto.of(null, attendance, passion, contactSpeed);
    }
}
