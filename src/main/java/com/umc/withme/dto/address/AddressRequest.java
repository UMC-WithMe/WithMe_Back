package com.umc.withme.dto.address;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Getter
public class AddressRequest {

    private String sido;
    private String sgg;

    public static AddressRequest of(String sido, String sgg) {
        return new AddressRequest(sido, sgg);
    }

    public AddressDto toDto() {
        return AddressDto.of(sido, sgg);
    }
}
