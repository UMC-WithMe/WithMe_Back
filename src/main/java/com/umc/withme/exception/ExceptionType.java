package com.umc.withme.exception;

import com.umc.withme.exception.common.CustomException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

@AllArgsConstructor
@Getter
public enum ExceptionType {

    // Common
    UNHANDLED_EXCEPTION(0, "알 수 없는 서버 에러가 발생했습니다."),
    METHOD_ARGUMENT_NOT_VALID_EXCEPTION(1, "요청 데이터가 잘못되었습니다."),

    // Address
    ADDRESS_NOT_FOUND_EXCEPTION(3400, "일치하는 주소를 찾을 수 없습니다."),

    // Member
    MEMBER_NICKNAME_IS_NULL_EXCEPTION(3500, "입력받은 닉네임 값이 올바르지 않습니다.");

    private final int errorCode;
    private final String errorMessage;
    private Class<? extends CustomException> type;

    ExceptionType(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public static ExceptionType from(Class<?> classType) {
        return Arrays.stream(values())
                .filter(it -> Objects.nonNull(it.type) && it.type.equals(classType))
                .findFirst()
                .orElse(UNHANDLED_EXCEPTION);
    }
}
