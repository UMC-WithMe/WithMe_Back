package com.umc.withme.domain;

import com.umc.withme.dto.member.MemberDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter
@Embeddable
public class TotalPoint {

    @Setter(AccessLevel.PRIVATE)
    @Column(nullable = false)
    private Integer attendanceTotalPoint;

    @Setter(AccessLevel.PRIVATE)
    @Column(nullable = false)
    private Integer passionTotalPoint;

    @Setter(AccessLevel.PRIVATE)
    @Column(nullable = false)
    private Integer contactTotalPoint;

    protected TotalPoint() {
        this.attendanceTotalPoint = 0;
        this.passionTotalPoint = 0;
        this.contactTotalPoint = 0;
    }

    /**
     * 출석, 열정, 연락 점수를 바탕으로 신뢰 점수를 계산한다.
     *
     * @param numOfReceivedReviews 받은 리뷰 수
     * @return 계산된 신뢰 점수
     */
    public double calculateTrustPoint(int numOfReceivedReviews) {
        if (numOfReceivedReviews == 0) {
            return 0.0;
        }

        Double attendanceAverage = this.getAttendanceTotalPoint() / (double) numOfReceivedReviews;
        Double passionAverage = this.getPassionTotalPoint() / (double) numOfReceivedReviews;
        Double contactAverage = this.getContactTotalPoint() / (double) numOfReceivedReviews;

        return (attendanceAverage + passionAverage + contactAverage) / 3.0;
    }
}
