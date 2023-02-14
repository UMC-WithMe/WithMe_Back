package com.umc.withme.dto.like;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "찜 삭제 요청 데이터")
public class MeetLikeDeleteRequest {
    @Schema(description = "삭제할 찜 id 리스트")
    List<Long> meetLikeIdList;
}
