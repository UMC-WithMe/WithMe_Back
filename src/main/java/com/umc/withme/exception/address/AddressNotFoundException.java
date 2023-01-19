package com.umc.withme.exception.address;

import com.umc.withme.exception.common.NotFoundException;

public class AddressNotFoundException extends NotFoundException {

    /**
     * 시/도, 시/군/구 정보를 전달받아 에러 로그에 함께 출력한다.
     */
    public AddressNotFoundException(String sido, String sgg) {
        super("sido=" + sido + " sgg=" + sgg);
    }
}
