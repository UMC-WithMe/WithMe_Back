package com.umc.withme.dto.member;

import com.umc.withme.domain.TotalPoint;
import com.umc.withme.dto.address.AddressDto;
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
    @Schema(description = "회원 닉네임")
    private String nickname;
    @Schema(description = "회원 신뢰도 지수")
    private Double trustPoint;
    @Schema(description = "회원 주소 中 시군구 정보")
    private String sggAddress;

    public static MemberShortInfoResponse of(MemberDto memberDto, int receivedTotalReviewCount, AddressDto addressDto){
        TotalPoint totalPoint = memberDto.getTotalPoint();
        Double attendanceAverage = totalPoint.getAttendanceTotalPoint()/(double)receivedTotalReviewCount;
        Double passionAverage = totalPoint.getPassionTotalPoint()/ (double)receivedTotalReviewCount;
        Double contactAverage = totalPoint.getContactTotalPoint()/(double)receivedTotalReviewCount;
        Double trustPoint = (attendanceAverage + passionAverage + contactAverage)/3;

        return new MemberShortInfoResponse(memberDto.getId(), memberDto.getNickname(), trustPoint, addressDto.getSgg());
    }

}
