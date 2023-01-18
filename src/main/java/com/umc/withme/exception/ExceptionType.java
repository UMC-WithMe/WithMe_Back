package com.umc.withme.exception;

import com.umc.withme.exception.address.AddressNotFoundException;
import com.umc.withme.exception.common.CustomException;
import com.umc.withme.exception.member.NicknameNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

@AllArgsConstructor
@Getter
public enum ExceptionType {

    // Common
    UNHANDLED_EXCEPTION(0, "알 수 없는 서버 에러가 발생했습니다.", null),
    BAD_REQUEST_EXCEPTION(1, "요청 데이터가 잘못되었습니다.", null),

    // Member
    NICKNAME_NOT_FOUND_EXCEPTION(1400, "해당 닉네임을 가지는 회원이 없습니다.", NicknameNotFoundException.class),

    // Address
    ADDRESS_NOT_FOUND_EXCEPTION(3400, "일치하는 주소를 찾을 수 없습니다.", AddressNotFoundException.class);

    private final int errorCode;
    private final String errorMessage;
    private Class<? extends CustomException> type;

    public static ExceptionType from(Class<?> classType) {
        return Arrays.stream(values())
                .filter(it -> Objects.nonNull(it.type) && it.type.equals(classType))
                .findFirst()
                .orElse(UNHANDLED_EXCEPTION);
    }
}
