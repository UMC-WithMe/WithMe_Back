package com.umc.withme.dto.auth;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc.withme.domain.constant.Gender;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("[DTO] Kakao 사용자 정보 응답 데이터 테스트")
class KakaoOAuthResponseTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void 인증_결과를_Map으로_받으면_카카오_인증_응답_객체로_변환한다() throws Exception {
        // given
        String response = "{" +
                "\"id\": 1234567890," +
                "\"connected_at\": \"2023-01-21T13:07:45Z\"," +
                "\"kakao_account\": {" +
                "\"has_email\": true," +
                "\"email_needs_agreement\": true," +
                "\"is_email_valid\": true," +
                "\"is_email_verified\": true," +
                "\"email\": \"test@daum.net\"," +
                "\"has_age_range\": true," +
                "\"age_range_needs_agreement\": true," +
                "\"age_range\": \"20~29\"," +
                "\"has_gender\": true," +
                "\"gender_needs_agreement\": true," +
                "\"gender\": \"male\"" +
                "}" +
                "}";
        Map<String, Object> attributes = mapper.readValue(response, new TypeReference<>() {
        });

        // when
        KakaoOAuthResponse result = KakaoOAuthResponse.from(attributes);

        // then
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(1234567890L),
                () -> assertThat(result.getConnectedAt()).isEqualTo(
                        ZonedDateTime.of(2023, 1, 21, 13, 7, 45, 0, ZoneOffset.UTC)
                                .withZoneSameInstant(ZoneId.systemDefault())
                                .toLocalDateTime()
                ),
                () -> assertThat(result.getEmail()).isEqualTo("test@daum.net"),
                () -> assertThat(result.getAgeRange()).isEqualTo(20),
                () -> assertThat(result.getGender()).isEqualTo(Gender.MALE)
        );
    }
}