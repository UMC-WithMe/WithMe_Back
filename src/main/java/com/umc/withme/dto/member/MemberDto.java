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
    private RoleType roleType;

    public static MemberDto of(String email, String password, Integer ageRange, Gender gender) {
        return MemberDto.of(null, null, email, password, null, null, ageRange, gender, null, null);
    }

    public static MemberDto of(Long id, AddressDto address, String email, String password, String phoneNumber, String nickname, Integer ageRange, Gender gender, TotalPoint totalPoint, RoleType roleType) {
        return new MemberDto(id, address, email, password, phoneNumber, nickname, ageRange, gender, totalPoint, roleType);
    }

    public static MemberDto from(Member member) {
        AddressDto addressDto = null;
        if (member.getAddress() != null) {
            addressDto = AddressDto.from(member.getAddress());
        }

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
                member.getRoleType()
        );
    }

    public Member toEntity(Address address) {
        return Member.builder()
                .email(this.email)
                .password(this.password)
                .ageRange(this.ageRange)
                .gender(this.gender)
                .build();
    }
}
