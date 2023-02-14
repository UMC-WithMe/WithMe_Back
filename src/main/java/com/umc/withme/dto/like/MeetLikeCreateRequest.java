package com.umc.withme.dto.like;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "찜 생성 요청 데이터")
public class MeetLikeCreateRequest {

    @Schema(description = "모집글 id(pk)")
    private Long meetId;
}
