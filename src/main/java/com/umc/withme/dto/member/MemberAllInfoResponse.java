package com.umc.withme.dto.member;

import com.umc.withme.domain.TotalPoint;
import com.umc.withme.domain.constant.Gender;
import com.umc.withme.dto.address.AddressDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Email;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Schema(description = "회원 전체 정보")
public class MemberAllInfoResponse {

    @Schema(example = "park", description = "회원의 닉네임")
    private String nickname;

    @Schema(example = "010-1234-5678", description = "회원의 핸드폰 번호")
    private String phoneNumber;

    @Schema(example = "20", description = "회원의 연령대")
    private Integer ageRange;

    @Schema(example = "FEMALE", description = "회원의 성별")
    private Gender gender;

    @Schema(description = "회원 이메일") @Email
    private String email;

    @Schema(description = "회원의 주소")
    private AddressDto address;

    @Schema(description = "항목별 점수 총합")
    private TotalPoint totalPoint;

    @Schema(description = "받은 리뷰 개수")
    private Long receivedReviewsCount;


    public static MemberAllInfoResponse of(MemberDto dto, Long receivedReviewsCount) {
        return new MemberAllInfoResponse(
                dto.getNickname(),
                dto.getPhoneNumber(),
                dto.getAgeRange(),
                dto.getGender(),
                dto.getEmail(),
                dto.getAddress(),
                dto.getTotalPoint(),
                receivedReviewsCount
        );
    }
}
