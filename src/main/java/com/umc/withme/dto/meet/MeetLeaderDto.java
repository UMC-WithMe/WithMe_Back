package com.umc.withme.dto.meet;

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

    private String email;
    private String nickName;
    private String phoneNumber;
    private LocalDate birth;
    private Gender gender;
    private TotalPoint totalPoint;

    public static MeetLeaderDto from(Member leader){
        return new MeetLeaderDto(leader.getEmail(), leader.getNickname(), leader.getPhoneNumber(),
                leader.getBirth(), leader.getGender(), leader.getTotalPoint());
    }

    public Member toEntity(){
        return Member.builder()
                .email(this.getEmail())
                .phoneNumber(this.getPhoneNumber())
                .birth(this.getBirth())
                .gender(this.getGender())
                .build();
    }

    public static MeetLeaderDto of(
            String email, String nickName, String phoneNumber,
            LocalDate birth, Gender gender, TotalPoint totalPoint
    ){
        return new MeetLeaderDto(email, nickName, phoneNumber, birth, gender, totalPoint);
    }
}
