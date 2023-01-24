package com.umc.withme.domain;

import com.umc.withme.domain.common.BaseTimeEntity;
import com.umc.withme.domain.constant.Gender;
import com.umc.withme.domain.constant.RoleType;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Setter
    @JoinColumn(name = "address_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Address address;

    @Column(unique = true, nullable = false)
    private String email;

    /**
     * <p> 일반 로그인 - 암호화된 password
     * <p> Kakao 로그인 - 회원번호
     */
    @Column(unique = true, nullable = false)
    private String password;

    @Setter
    @Column(unique = true)
    private String phoneNumber;

    @Column(unique = true, nullable = false)
    private String nickname;

    @Column(nullable = false)
    private Integer ageRange;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Embedded
    private TotalPoint totalPoint;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    // Builder & Constructor
    @Builder
    private Member(String email, String password, Integer ageRange, Gender gender) {
        this.email = email;
        this.password = password;
        this.nickname = email;
        this.ageRange = ageRange;
        this.gender = gender;
        this.totalPoint = new TotalPoint();
        this.roleType = RoleType.USER;
    }

    // 코드 추가는 여기에

    // Equals and HashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Member)) return false;
        Member that = (Member) o;
        return this.getId() != null && this.getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }
}
