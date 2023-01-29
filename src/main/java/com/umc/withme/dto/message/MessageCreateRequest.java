package com.umc.withme.dto.message;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Schema(description = "쪽지 생성 요청 데이터")
public class MessageCreateRequest {

    @Schema(description = "쪽지 내용")
    @NotBlank @Size(max = 500)
    private String content;

}
