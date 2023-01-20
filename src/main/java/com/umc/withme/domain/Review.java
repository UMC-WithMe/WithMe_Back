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
public class Review extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member sender_id;

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member receiver_id;

    @JoinColumn(name = "meet_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Meet meet;

    @JoinColumn(name = "point_id", nullable = false)
    @OneToOne(fetch = FetchType.LAZY)
    private Point point;

    @Column(length = 250, nullable = false)
    private String content;

    // Builder & Constructor
    @Builder
    public Review(Member sender_id, Member receiver_id, Meet meet, Point point, String content) {
        this.sender_id = sender_id;
        this.receiver_id = receiver_id;
        this.meet = meet;
        this.point = point;
        this.content = content;
    }

    // 코드 추가는 여기에

    // Equals and HashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Review)) return false;
        Review that = (Review) o;
        return this.getId() != null && this.getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }
}
