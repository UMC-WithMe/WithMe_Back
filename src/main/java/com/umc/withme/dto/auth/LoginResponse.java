package com.umc.withme.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class LoginResponse {

    @Schema(example = "true", description = "휴대폰 번호 등록 및 인증 여부")
    private boolean phoneNumberExistence;

    @Schema(example = "true", description = "주소 등록 및 인증 여부")
    private boolean addressExistence;

    @Schema(
            example = "35Vu7VBqtRgBQ9GjdIt9onYS03yXuAP28R2HLRMxKVO9wrEdJ5Fj8y0r3b6I7QJYjApZG3JuOA28YUDjwkdYqOxj2s3f94DdQjww",
            description = "Jwt access token"
    )
    private String accessToken;

    public static LoginResponse of(boolean phoneNumberExistence, boolean addressExistence, String accessToken) {
        return new LoginResponse(phoneNumberExistence, addressExistence, accessToken);
    }
}
