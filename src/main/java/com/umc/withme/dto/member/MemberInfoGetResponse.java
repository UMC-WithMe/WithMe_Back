package com.umc.withme.dto.member;

import com.umc.withme.domain.constant.Gender;
import com.umc.withme.dto.address.AddressDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class MemberInfoGetResponse {

    private String nickname;
    private String phoneNumber;
    private LocalDate birth;
    private Gender gender;
    private AddressDto address;

    public static MemberInfoGetResponse from(MemberDto dto) {
        return new MemberInfoGetResponse(
                dto.getNickname(),
                dto.getPhoneNumber(),
                dto.getBirth(),
                dto.getGender(),
                dto.getAddress()
        );
    }
}
