package com.umc.withme.dto.address;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Getter
public class AddressRequest {

    @Schema(example = "서울특별시", description = "주소의 시/도 정보")
    private String sido;

    @Schema(example = "강남구", description = "주소의 시/군/구 정보")
    private String sgg;

    public static AddressRequest of(String sido, String sgg) {
        return new AddressRequest(sido, sgg);
    }

    public AddressDto toDto() {
        return AddressDto.of(sido, sgg);
    }
}
