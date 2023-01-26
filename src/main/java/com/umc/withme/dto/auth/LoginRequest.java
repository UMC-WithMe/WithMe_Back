package com.umc.withme.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class LoginRequest {

    @Schema(
            example = "ZKvMpru4xZOETYhWeyc2Ved0q4AUdXUFr4KBzh7DtQSKNKVvb4FquEyb",
            description = "Kakao server에서 전달받은 access token"
    )
    @NotBlank
    private String kakaoAccessToken;

    public static LoginRequest of(String kakaoAccessToken) {
        return new LoginRequest(kakaoAccessToken);
    }
}
