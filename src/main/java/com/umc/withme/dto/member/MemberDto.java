package com.umc.withme.dto.member;

import com.umc.withme.domain.Member;
import com.umc.withme.domain.TotalPoint;
import com.umc.withme.domain.constant.Gender;
import com.umc.withme.domain.constant.RoleType;
import com.umc.withme.dto.address.AddressDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class MemberDto {

    private Long id;
    private AddressDto address;
    private String email;
    private String password;
    private String phoneNumber;
    private String nickname;
    private Integer ageRange;
    private Gender gender;
    private TotalPoint totalPoint;
    private Integer numOfReceivedReviews;
    private RoleType roleType;

    public static MemberDto of(String email, String password, Integer ageRange, Gender gender) {
        return MemberDto.of(null, null, email, password, null, null, ageRange, gender, null, null, null);
    }

    public static MemberDto of(Long id, AddressDto address, String email, String password, String phoneNumber, String nickname, Integer ageRange, Gender gender, TotalPoint totalPoint, Integer numOfReceivedReviews, RoleType roleType) {
        return new MemberDto(id, address, email, password, phoneNumber, nickname, ageRange, gender, totalPoint, numOfReceivedReviews, roleType);
    }

    public static MemberDto from(Member member) {
        AddressDto addressDto = member.getAddress() != null ? AddressDto.from(member.getAddress()) : null;

        return MemberDto.of(
                member.getId(),
                addressDto,
                member.getEmail(),
                member.getPassword(),
                member.getPhoneNumber(),
                member.getNickname(),
                member.getAgeRange(),
                member.getGender(),
                member.getTotalPoint(),
                member.getNumOfReceivedReviews(),
                member.getRoleType()
        );
    }

    public Member toEntity() {
        return Member.builder()
                .email(this.email)
                .password(this.password)
                .ageRange(this.ageRange)
                .gender(this.gender)
                .build();
    }
}
