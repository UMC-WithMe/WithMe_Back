package com.umc.withme.domain;

import com.umc.withme.domain.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @JoinColumn(name = "sender_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member sender;

    @JoinColumn(name = "receiver_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member receiver;

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
    private Review(Member sender, Member receiver, Meet meet, Point point, String content) {
        this.sender = sender;
        this.receiver = receiver;
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
