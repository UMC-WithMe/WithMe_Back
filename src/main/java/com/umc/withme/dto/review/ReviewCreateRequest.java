package com.umc.withme.dto.review;

import com.umc.withme.dto.point.PointRequest;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ReviewCreateRequest {

    @NotNull
    private PointRequest point;
    @NotEmpty
    private String content;
}
