package com.umc.withme.dto.member;

import com.umc.withme.domain.Address;
import com.umc.withme.domain.Member;
import com.umc.withme.domain.constant.Gender;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class MemberDto {

    private String nickname;
    private String phoneNumber;
    private LocalDate birth;
    private Gender gender;
    private Address address;

    public static MemberDto from(Member member) {
        return new MemberDto(member.getNickname(), member.getPhoneNumber(), member.getBirth(), member.getGender(), member.getAddress());
    }
}