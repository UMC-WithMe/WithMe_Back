package com.umc.withme.dto.point;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class PointRequest {

    @NotNull
    private Integer attendance;

    @NotNull
    private Integer passion;

    @NotNull
    private Integer contactSpeed;

    public PointDto toDto() {
        return PointDto.of(null, attendance, passion, contactSpeed);
    }
}
