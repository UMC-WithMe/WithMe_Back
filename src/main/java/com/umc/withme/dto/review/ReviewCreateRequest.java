package com.umc.withme.dto.review;

import com.umc.withme.dto.point.PointRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ReviewCreateRequest {

    @Schema(description = "회원 신뢰도 평가 3가지")
    @Valid
    private PointRequest point;

    @Schema(example = "이 회원은 모임에 열정적이었지만 연락속도가 느립니다.", description = "후기 내용")
    @NotBlank
    @Length(max = 250)
    private String content;
}
