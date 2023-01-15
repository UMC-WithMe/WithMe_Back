package com.umc.withme.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter
@Embeddable
public class TotalPoint {

    @Setter
    @Column(nullable = false)
    private Integer attendanceTotalPoint;

    @Setter
    @Column(nullable = false)
    private Integer passionTotalPoint;

    @Setter
    @Column(nullable = false)
    private Integer contactTotalPoint;

    protected TotalPoint() {
        this.attendanceTotalPoint = 0;
        this.passionTotalPoint = 0;
        this.contactTotalPoint = 0;
    }
}
