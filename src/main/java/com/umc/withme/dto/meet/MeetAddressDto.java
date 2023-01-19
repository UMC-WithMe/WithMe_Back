package com.umc.withme.dto.meet;

import com.umc.withme.domain.Address;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MeetAddressDto {
    private String sido;
    private String sgg;

    public static MeetAddressDto from(Address address){
        return new MeetAddressDto(address.getSido(), address.getSgg());
    }

    public Address toEntity(){
        return new Address(sido, sgg);
    }

    public static MeetAddressDto of(String sido, String sgg){
        return new MeetAddressDto(sido, sgg);
    }
}
