package com.umc.withme.dto.review;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ReviewGetInfoResponse {

    @NotBlank
    private String nickname;

    @NotBlank
    private String content;

    @NotBlank
    private String meetTitle;

    @NotNull
    private LocalDateTime createAt;

    public static ReviewGetInfoResponse of(String nickname, String content, String meetTitle, LocalDateTime createAt) {
        return new ReviewGetInfoResponse(nickname, content, meetTitle, createAt);
    }
}
