package com.umc.withme.dto.member.response;

import com.umc.withme.dto.member.MemberDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(description = "회원 대표적인 프로필 정보")
public class MemberShortInfoResponse {

    @Schema(description = "회원 id(pk)")
    private Long memberId;
    @Schema(example = "https://withme-s3-bucket.s3.ap-northeast-2.amazonaws.com/member/default-profile-image.jpeg", description = "회원 프로필 이미지 URL")
    private String profileImage;
    @Schema(description = "회원 닉네임")
    private String nickname;
    @Schema(description = "회원 신뢰도 지수")
    private Double trustPoint;
    @Schema(description = "회원 주소 中 시군구 정보")
    private String sggAddress;

    public static MemberShortInfoResponse from(MemberDto memberDto) {
        double trustPoint = memberDto.getTotalPoint().calculateTrustPoint(memberDto.getNumOfReceivedReviews());
        return new MemberShortInfoResponse(
                memberDto.getId(),
                memberDto.getProfileImage().getUrl(),
                memberDto.getNickname(),
                trustPoint,
                memberDto.getAddress() != null ? memberDto.getAddress().getSgg() : null
        );
    }
}
