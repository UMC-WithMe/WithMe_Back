package com.umc.withme.dto.member;

import com.umc.withme.domain.Address;
import com.umc.withme.domain.Member;
import com.umc.withme.domain.TotalPoint;
import com.umc.withme.domain.constant.Gender;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class MemberDto {

    private Long id;
    private Address address;
    private String email;
    private String phoneNumber;
    private String nickname;
    private LocalDate birth;
    private Gender gender;
    private TotalPoint totalPoint;

    public static MemberDto of(Long id, Address address, String email, String phoneNumber, String nickname, LocalDate birth, Gender gender, TotalPoint totalPoint) {
        return new MemberDto(id, address, email, phoneNumber, nickname, birth, gender, totalPoint);
    }

    public static MemberDto from(Member member) {
        return MemberDto.of(
                member.getId(),
                member.getAddress(),
                member.getEmail(),
                member.getPhoneNumber(),
                member.getNickname(),
                member.getBirth(),
                member.getGender(),
                member.getTotalPoint()
        );
    }

    public Member toEntity() {
        return Member.builder()
                .address(address)
                .email(email)
                .phoneNumber(phoneNumber)
                .birth(birth)
                .gender(gender)
                .build();
    }
}