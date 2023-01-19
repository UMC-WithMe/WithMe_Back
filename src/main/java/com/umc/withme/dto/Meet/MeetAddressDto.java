package com.umc.withme.dto.Meet;

import com.umc.withme.domain.Address;
import com.umc.withme.domain.MeetAddress;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class MeetAddressDto {
    private String sido;
    private String sgg;

    /**
     * Address Entity인 Meet의 address를 입력받아
     * 이를 MeatAddressDto로 변환해서 반환해주는 함수이다.
     * @param address
     * @return 변환된 MeetAddressDto
     */
    public static MeetAddressDto from(Address address){
        return MeetAddressDto.builder()
                .sido(address.getSido())
                .sgg(address.getSgg())
                .build();
    }

    /**
     * MeetAddressDto 인스턴스를 Address Entity로 변환하여 반환해주는 함수이다.
     * @return 변환된 Address Entity
     */
    public Address toEntity(){
        return new Address(sido, sgg);
    }

    /**
     * 입력받은 시도, 시군구 정보를 바탕으로 MeetAddressDto 인스턴스를 생성해 반환해주는 함수이다.
     * @param sido
     * @param sgg
     * @return 생성한 MeetAddressDto 인스턴스
     */
    public static MeetAddressDto of(String sido, String sgg){
        return MeetAddressDto.builder()
                .sido(sido)
                .sgg(sgg)
                .build();
    }

    @Override
    public String toString() {
        return "MeetAddressDto{" +
                "sido='" + sido + '\'' +
                ", sgg='" + sgg + '\'' +
                '}';
    }
}
