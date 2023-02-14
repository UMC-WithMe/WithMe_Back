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
public class MeetLike extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meet_like_id")
    private Long id;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @JoinColumn(name = "meet_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Meet meet;

    @Builder
    public MeetLike(Member member, Meet meet){
        this.member = member;
        this.meet = meet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MeetLike)) return false;
        MeetLike that = (MeetLike) o;
        return this.getId() != null && this.getId().equals(that.getId());
    }

    @Override
    public int hashCode() { return Objects.hash(this.getId()); }

}
