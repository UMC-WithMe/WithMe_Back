package com.umc.withme.domain;

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
}
