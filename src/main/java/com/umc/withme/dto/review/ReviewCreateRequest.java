package com.umc.withme.dto.review;

import com.umc.withme.dto.point.PointRequest;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ReviewCreateRequest {

    @Valid
    private PointRequest point;

    @NotBlank
    @Length(max = 250)
    private String content;
}
