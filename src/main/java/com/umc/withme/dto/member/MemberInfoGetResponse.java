package com.umc.withme.dto.member;

import com.umc.withme.domain.constant.Gender;
import com.umc.withme.dto.address.AddressDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class MemberInfoGetResponse {

    @Schema(example = "park", description = "회원의 닉네임")
    private String nickname;

    @Schema(example = "010-1234-5678", description = "회원의 핸드폰 번호")
    private String phoneNumber;

    @Schema(example = "20", description = "회원의 연령대")
    private Integer ageRange;

    @Schema(example = "FEMALE", description = "회원의 성별")
    private Gender gender;

    @Schema(description = "회원의 주소")
    private AddressDto address;

    public static MemberInfoGetResponse from(MemberDto dto) {
        return new MemberInfoGetResponse(
                dto.getNickname(),
                dto.getPhoneNumber(),
                dto.getAgeRange(),
                dto.getGender(),
                dto.getAddress()
        );
    }
}
