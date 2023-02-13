package com.umc.withme.exception;

import com.umc.withme.exception.address.AddressNotFoundException;
import com.umc.withme.exception.auth.KakaoOAuthUnauthorizedException;
import com.umc.withme.exception.common.CustomException;
import com.umc.withme.exception.file.MultipartFileNotReadableException;
import com.umc.withme.exception.meet.MeetDeleteForbiddenException;
import com.umc.withme.exception.meet.MeetIdNotFoundException;
import com.umc.withme.exception.meet.MeetUpdateForbiddenException;
import com.umc.withme.exception.member.EmailNotFoundException;
import com.umc.withme.exception.member.MemberIdNotFoundException;
import com.umc.withme.exception.member.NicknameDuplicateException;
import com.umc.withme.exception.member.NicknameNotFoundException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

/**
 * [ Error code 규약 ]
 * Member: 1XXX
 * Meet: 2XXX
 * Address: 3XXX
 */

@AllArgsConstructor
@Getter
public enum ExceptionType {

    // Common
    UNHANDLED_EXCEPTION(0, "알 수 없는 서버 에러가 발생했습니다.", null),
    BAD_REQUEST_EXCEPTION(1, "요청 데이터가 잘못되었습니다.", null),
    ACCESS_DENIED_EXCEPTION(2, "권한이 유효하지 않습니다.", null),
    AUTHENTICATION_EXCEPTION(3, "인증 과정에서 문제가 발생했습니다.", null),

    // Auth
    JWT_INVALID_SIGNATURE_EXCEPTION(100, "Token의 서명이 잘못되었습니다.", SignatureException.class),
    JWT_EXPIRED_EXCEPTION(101, "Token이 만료되었습니다.", ExpiredJwtException.class),
    JWT_MALFORMED_EXCEPTION(102, "유효하지 않은 token입니다.", MalformedJwtException.class),
    JWT_UNSUPPORTED_EXCEPTION(103, "처리할 수 없는 token입니다.", UnsupportedJwtException.class),
    KAKAO_OAUTH_UNAUTHORIZED_EXCEPTION(104, "카카오 인증 과정에서 문제가 발생했습니다. Kakao access token이 만료되지는 않았는지 확인해주세요.", KakaoOAuthUnauthorizedException.class),

    // Member
    NICKNAME_NOT_FOUND_EXCEPTION(1400, "해당 닉네임을 가지는 회원이 없습니다.", NicknameNotFoundException.class),
    EMAIL_NOT_FOUND_EXCEPTION(1401, "해당 이메일을 갖는 회원을 찾을 수 없습니다.", EmailNotFoundException.class),
    MEMBER_ID_NOT_FOUND_EXCEPTION(1402, "해당 아이디(PK)를 가지는 회원이 없습니다.", MemberIdNotFoundException.class),
    NICKNAME_DUPLICATE_EXCEPTION(1403, "이미 사용중인 닉네임입니다.", NicknameDuplicateException.class),

    // Meet
    MEET_ID_NOT_FOUND_EXCEPTION(2400, "해당 아이디(PK)를 가지는 모임이 없습니다.", MeetIdNotFoundException.class),
    MEET_DELETE_FORBIDDEN_EXCEPTION(2401, "해당 모임을 삭제할 권한이 없습니다.", MeetDeleteForbiddenException.class),
    MEET_UPDATE_FORBIDDEN_EXCEPTION(2402, "해당 모임을 수정할 권한이 없습니다.", MeetUpdateForbiddenException.class),

    // Address
    ADDRESS_NOT_FOUND_EXCEPTION(3400, "일치하는 주소를 찾을 수 없습니다.", AddressNotFoundException.class),

    // File
    MULTIPART_FILE_NOT_READABLE_EXCEPTION(4400, "파일을 읽을 수 없습니다.", MultipartFileNotReadableException.class);

    private final int errorCode;
    private final String errorMessage;
    private final Class<? extends Exception> type;

    public static ExceptionType from(Class<? extends CustomException> classType) {
        return findExceptionType(classType, UNHANDLED_EXCEPTION);
    }

    public static ExceptionType fromByAuthenticationFailed(Class<? extends Exception> classType) {
        return findExceptionType(classType, AUTHENTICATION_EXCEPTION);
    }

    private static ExceptionType findExceptionType(
            Class<? extends Exception> classType,
            ExceptionType defaultExceptionType
    ) {
        return Arrays.stream(values())
                .filter(it -> Objects.nonNull(it.type) && it.type.equals(classType))
                .findFirst()
                .orElse(defaultExceptionType);
    }
}
