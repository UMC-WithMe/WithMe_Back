package com.umc.withme.dto.Meet;

import com.umc.withme.domain.Member;
import com.umc.withme.domain.TotalPoint;
import com.umc.withme.domain.constant.Gender;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class MeetLeaderDto {

    private String email;
    private String nickName;
    private String phoneNumber;
    private LocalDate birth;
    private Gender gender;
    private TotalPoint totalPoint;

    /**
     * Member Entity인 Meet의 leader를 입력받아
     * 이를 MeetLeaderDto로 변환해서 반환해주는 함수이다.
     * @param  변환하려 하는 모임의 리더
     * @return 변환된 MeetLeaderDto
     */
    public static MeetLeaderDto from(Member leader){
        return MeetLeaderDto.builder()
                .email(leader.getEmail())
                .nickName(leader.getNickname())
                .phoneNumber(leader.getPhoneNumber())
                .birth(leader.getBirth())
                .gender(leader.getGender())
                .totalPoint(leader.getTotalPoint())
                .build();
    }

    /**
     * MeatLeaderDto 객체를 Member Entity 로 변환하여 반환해주는 함수이다.
     * @return 변환된 Member Entity
     */
    public Member toEntity(){
        return Member.builder()
                .email(this.getEmail())
                .phoneNumber(this.getPhoneNumber())
                .birth(this.getBirth())
                .gender(this.getGender())
                .build();
    }

    /**
     * 입력받은 파라미터를 바탕으로 MeetLeaderDto 인스턴스를 생성해 반환해주는 함수이다.
     * @param email
     * @param nickName
     * @param phoneNumber
     * @param birth
     * @param gender
     * @param totalPoint
     * @return 생성한 MeetLeaderDto 인스턴스
     */
    public static MeetLeaderDto of(
            String email, String nickName, String phoneNumber,
            LocalDate birth, Gender gender, TotalPoint totalPoint
    ){
        return MeetLeaderDto.builder()
                .email(email)
                .nickName(nickName)
                .phoneNumber(phoneNumber)
                .birth(birth)
                .gender(gender)
                .totalPoint(totalPoint)
                .build();
    }
}