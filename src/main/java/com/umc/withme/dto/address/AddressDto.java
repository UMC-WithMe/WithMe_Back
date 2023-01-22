package com.umc.withme.dto.address;

import com.umc.withme.domain.Address;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class AddressDto {

    private String sido;
    private String sgg;

    public static AddressDto of(String sido, String sgg) {
        return new AddressDto(sido, sgg);
    }

    public static AddressDto from(Address address) {
        return AddressDto.of(
                address.getSido(),
                address.getSgg()
        );
    }
}
