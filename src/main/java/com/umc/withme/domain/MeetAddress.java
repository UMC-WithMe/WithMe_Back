package com.umc.withme.domain;

import com.umc.withme.domain.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class MeetAddress extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meet_address_id")
    private Long id;

    @JoinColumn(name = "meet_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Meet meet;

    @JoinColumn(name = "address_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Address address;

    // Builder & Constructor
    public MeetAddress(Meet meet, Address address){
        this.meet = meet;
        this.address = address;
    }

    // 코드 추가는 여기에

    // Equals and HashCode
    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (!(o instanceof MeetAddress)) return false;
        MeetAddress that = (MeetAddress) o;
        return this.getId() != null && this.getId().equals(that.getId());
    }

    @Override
    public int hashCode() { return Objects.hash(this.getId()); }
}
