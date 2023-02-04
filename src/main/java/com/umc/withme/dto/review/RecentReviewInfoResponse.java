package com.umc.withme.dto.review;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class RecentReviewInfoResponse {

    @Schema(description = "가장 최근에 끝난 모임에서 받은 후기 목록")
    private List<RecentReviewInfo> info1;

    @Schema(description = "2번째로 최근에 끝난 모임에서 받은 후기 목록")
    private List<RecentReviewInfo> info2;

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    public static class RecentReviewInfo {

        @Schema(example = "스프링 스터디", description = "참여한 모임 이름")
        private String meetTitle;

        @Schema(example = "2023-01-01", description = "모임 시작 날짜")
        private LocalDate startDate;

        @Schema(example = "2023-03-01", description = "모임 종료 날짜")
        private LocalDate endDate;

        @Schema(example = "park", description = "후기를 작성한 회원의 닉네임")
        private String nickname;

        @Schema(example = "이 회원은 모임에 열정적이었지만 연락속도가 느립니다.", description = "후기 내용")
        private String content;

        public static RecentReviewInfo from(ReviewDto dto) {
            return new RecentReviewInfo(
                    dto.getMeet().getTitle(),
                    dto.getMeet().getStartDate(),
                    dto.getMeet().getEndDate(),
                    dto.getSender().getNickname(),
                    dto.getContent()
            );
        }
    }

    public static RecentReviewInfoResponse from(List<ReviewDto> dto1, List<ReviewDto> dto2) {
        return new RecentReviewInfoResponse(
                dto1.stream().map(RecentReviewInfo::from).collect(Collectors.toUnmodifiableList()),
                dto2.stream().map(RecentReviewInfo::from).collect(Collectors.toUnmodifiableList())
        );
    }
}
