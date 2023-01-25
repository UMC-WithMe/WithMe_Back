package com.umc.withme.repository;

import com.umc.withme.domain.Address;
import com.umc.withme.exception.address.AddressNotFoundException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@Disabled("Spring Data JPA가 만들어주는 기능은 테스트 할 필요가 없다.")
@DisplayName("[Repository] Address")
@DataJpaTest
class AddressRepositoryTest {

    private final AddressRepository addressRepository;

    public AddressRepositoryTest(@Autowired AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @MethodSource(value = "sggTestData")
    @ParameterizedTest(name = "[{index}] {0} {1} => PK: {2}")
    void 시도_시군구_정보가_주어지면_주소를_조회한다(String sido, String sgg, Long addressId) {
        // given

        // when
        Address actual = addressRepository.findBySidoAndSgg(sido, sgg)
                .orElseThrow(() -> new AddressNotFoundException(sido, sgg));

        // then
        assertThat(actual.getId()).isEqualTo(addressId);
    }

    @Test
    void 잘못된_정보로_주소를_조회하면_예외가_발생한다() {
        // given
        String sido = "시도";
        String sgg = "시군구";

        // when
        Throwable t = catchThrowable(() ->
                addressRepository.findBySidoAndSgg(sido, sgg)
                        .orElseThrow(() -> new AddressNotFoundException(sido, sgg))
        );

        // then
        assertThat(t)
                .isInstanceOf(AddressNotFoundException.class);
    }

    static Stream<Arguments> sggTestData() {
        return Stream.of(
                arguments("서울특별시", "종로구", 1L),
                arguments("서울특별시", "강남구", 23L),
                arguments("부산광역시", "해운대구", 34L),
                arguments("인천광역시", "중구", 50L),
                arguments("대전광역시", "서구", 67L),
                arguments("경기도", "수원시 팔달구", 79L),
                arguments("경기도", "수원시 영통구", 80L),
                arguments("충청북도", "옥천군", 143L),
                arguments("전라남도", "보성군", 190L),
                arguments("경상남도", "창원시 의창구", 234L)
        );
    }
}
