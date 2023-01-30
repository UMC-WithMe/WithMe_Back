package com.umc.withme.dto.review;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ReviewGetInfoResponse {

    @Schema(example = "park", description = "후기를 작성한 회원의 닉네임")
    @NotBlank
    private String nickname;

    @Schema(example = "이 회원은 모임에 열정적이었지만 연락속도가 느립니다.", description = "후기 내용")
    @NotBlank
    private String content;

    @Schema(example = "스프링 스터디", description = "함께 참여한 모임 이름")
    @NotBlank
    private String meetTitle;

    @Schema(example = "2023-01-30T11:30:53.690336", description = "후기 작성 시간")
    @NotNull
    private LocalDateTime createAt;

    public static ReviewGetInfoResponse of(String nickname, String content, String meetTitle, LocalDateTime createAt) {
        return new ReviewGetInfoResponse(nickname, content, meetTitle, createAt);
    }
}
