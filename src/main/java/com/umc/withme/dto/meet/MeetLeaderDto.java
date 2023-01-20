package com.umc.withme.dto.meet;

import com.umc.withme.domain.Address;
import com.umc.withme.domain.Member;
import com.umc.withme.domain.TotalPoint;
import com.umc.withme.domain.constant.Gender;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MeetLeaderDto {
    private Address address;
    private String email;
    private String nickName;
    private String phoneNumber;
    private LocalDate birth;
    private Gender gender;
    private TotalPoint totalPoint;

    public static MeetLeaderDto from(Member leader){
        return new MeetLeaderDto( leader.getAddress(),
                leader.getEmail(), leader.getNickname(), leader.getPhoneNumber(),
                leader.getBirth(), leader.getGender(), leader.getTotalPoint());
    }

    public Member toEntity(){
        return Member.builder()
                .address(this.getAddress())
                .email(this.getEmail())
                .phoneNumber(this.getPhoneNumber())
                .birth(this.getBirth())
                .gender(this.getGender())
                .build();
    }

    public static MeetLeaderDto of(
            Address address,
            String email, String nickName, String phoneNumber,
            LocalDate birth, Gender gender, TotalPoint totalPoint
    ){
        return new MeetLeaderDto(address, email, nickName, phoneNumber, birth, gender, totalPoint);
    }
}
