package com.umc.withme.domain;

import com.umc.withme.domain.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Point extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "point_id")
    private Long id;

    @Column(nullable = false)
    private Integer attendance;

    @Column(nullable = false)
    private Integer passion;

    @Column(nullable = false)
    private Integer contactSpeed;

    // Builder & Constructor
    @Builder
    private Point(Integer attendance, Integer passion, Integer contactSpeed) {
        this.attendance = attendance;
        this.passion = passion;
        this.contactSpeed = contactSpeed;
    }

    // 코드 추가는 여기에

    // Equals and HashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Point)) return false;
        Point that = (Point) o;
        return this.getId() != null && this.getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }
}
