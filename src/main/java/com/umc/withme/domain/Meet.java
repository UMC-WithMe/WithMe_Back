package com.umc.withme.domain;

import com.umc.withme.domain.common.BaseEntity;
import com.umc.withme.domain.constant.MeetCategory;
import com.umc.withme.domain.constant.MeetStatus;
import com.umc.withme.domain.constant.RecruitStatus;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Meet extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meet_id")
    private Long id;

    @JoinColumn(name = "leader_id", nullable = false)
    @OneToOne(fetch = FetchType.LAZY)
    @Setter
    private Member leader;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MeetCategory category;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RecruitStatus recruitStatus;

    @Enumerated(EnumType.STRING)
    private MeetStatus meetStatus;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Integer minPeople;

    @Column(nullable = false)
    private Integer maxPeople;

    private String link;

    @Column(length = 2000, nullable = false)
    private String content;

    private LocalDate startDate;

    private LocalDate endDate;

    // Builder & Constructor
    @Builder
    private Meet(Member leader, MeetCategory category, String title, Integer minPeople, Integer maxPeople, String link, String content) {
        this.leader = leader;
        this.category = category;
        this.recruitStatus = RecruitStatus.PROGRESS;
        this.title = title;
        this.minPeople = minPeople;
        this.maxPeople = maxPeople;
        this.link = link;
        this.content = content;
    }

    // 코드 추가는 여기에

    // Equals and HashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Meet)) return false;
        Meet that = (Meet) o;
        return this.getId() != null && this.getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }
}
