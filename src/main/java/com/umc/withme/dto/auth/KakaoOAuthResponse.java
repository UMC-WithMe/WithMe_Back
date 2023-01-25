package com.umc.withme.dto.auth;

import com.umc.withme.domain.constant.Gender;
import com.umc.withme.dto.member.MemberDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@SuppressWarnings("unchecked")  // TODO: Map -> Object 변환 로직이 있어서 generic type casting 문제를 무시한다. 더 좋은 방법이 있다면 고려할 수 있음.
public class KakaoOAuthResponse {

    private Long id;    // Kakao 회원 번호
    private LocalDateTime connectedAt;
    private KakaoAccount kakaoAccount;

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter(AccessLevel.PRIVATE)
    public static class KakaoAccount {
        private Boolean hasEmail;
        private Boolean emailNeedsAgreement;
        private Boolean isEmailValid;
        private Boolean isEmailVerified;
        private String email;
        private Boolean hasAgeRange;
        private Boolean ageRangeNeedsAgreement;
        private Integer ageRange;
        private Boolean hasGender;
        private Boolean genderNeedsAgreement;
        private Gender gender;

        public static KakaoAccount of(Boolean hasEmail, Boolean emailNeedsAgreement, Boolean isEmailValid, Boolean isEmailVerified, String email, Boolean hasAgeRange, Boolean ageRangeNeedsAgreement, Integer ageRange, Boolean hasGender, Boolean genderNeedsAgreement, Gender gender) {
            return new KakaoAccount(hasEmail, emailNeedsAgreement, isEmailValid, isEmailVerified, email, hasAgeRange, ageRangeNeedsAgreement, ageRange, hasGender, genderNeedsAgreement, gender);
        }

        public static KakaoAccount from(Map<String, Object> attributes) {
            return new KakaoAccount(
                    Boolean.valueOf(String.valueOf(attributes.get("has_email"))),
                    Boolean.valueOf(String.valueOf(attributes.get("email_needs_agreement"))),
                    Boolean.valueOf(String.valueOf(attributes.get("is_email_valid"))),
                    Boolean.valueOf(String.valueOf(attributes.get("is_email_verified"))),
                    String.valueOf(attributes.get("email")),
                    Boolean.valueOf(String.valueOf(attributes.get("has_age_range"))),
                    Boolean.valueOf(String.valueOf(attributes.get("age_range_needs_agreement"))),
                    Integer.valueOf(String.valueOf(attributes.get("age_range")).substring(0, 2)),
                    Boolean.valueOf(String.valueOf(attributes.get("has_gender"))),
                    Boolean.valueOf(String.valueOf(attributes.get("gender_needs_agreement"))),
                    Gender.caseFreeValueOf(String.valueOf(attributes.get("gender")))
            );
        }
    }

    public static KakaoOAuthResponse of(Long id, LocalDateTime connectedAt, KakaoAccount kakaoAccount) {
        return new KakaoOAuthResponse(id, connectedAt, kakaoAccount);
    }

    public static KakaoOAuthResponse from(Map<String, Object> attributes) {
        return new KakaoOAuthResponse(
                Long.valueOf(String.valueOf(attributes.get("id"))),
                ZonedDateTime.parse(
                        String.valueOf(attributes.get("connected_at")),
                        DateTimeFormatter.ISO_INSTANT.withZone(ZoneId.systemDefault())
                ).toLocalDateTime(),
                KakaoAccount.from((Map<String, Object>) attributes.get("kakao_account"))
        );
    }

    public MemberDto toMemberDto() {
        return MemberDto.of(
                getEmail(),
                String.valueOf(getId()),
                getAgeRange(),
                getGender()
        );
    }

    // Getter
    public String getEmail() {
        return this.getKakaoAccount().getEmail();
    }

    public Integer getAgeRange() {
        return this.getKakaoAccount().getAgeRange();
    }

    public Gender getGender() {
        return this.getKakaoAccount().getGender();
    }
}
