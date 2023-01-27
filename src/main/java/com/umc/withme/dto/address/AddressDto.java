package com.umc.withme.dto.address;

import com.umc.withme.domain.Address;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class AddressDto {

    @Schema(example = "서울특별시", description = "주소의 시/도 정보")
    private String sido;

    @Schema(example = "강남구", description = "주소의 시/군/구 정보")
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
