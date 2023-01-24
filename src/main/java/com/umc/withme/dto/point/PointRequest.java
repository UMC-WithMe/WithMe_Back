package com.umc.withme.dto.point;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class PointRequest {

    private Integer attendance;
    private Integer passion;
    private Integer contactSpeed;

    public PointDto toDto() {
        return PointDto.of(null, attendance, passion, contactSpeed);
    }
}
