package com.umc.withme.dto.review;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class RecentReviewInfoResponse {

    private List<RecentReviewInfo> info1;
    private List<RecentReviewInfo> info2;

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    public static class RecentReviewInfo {

        private String meetTitle;
        private LocalDate startDate;
        private LocalDate endDate;
        private String nickname;
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
