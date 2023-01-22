package com.umc.withme.service;

import com.umc.withme.domain.Address;
import com.umc.withme.domain.Member;
import com.umc.withme.domain.constant.Gender;
import com.umc.withme.dto.member.MemberDto;
import com.umc.withme.exception.member.EmailNotFoundException;
import com.umc.withme.exception.member.NicknameNotFoundException;
import com.umc.withme.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@DisplayName("[Service] Member")
@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    private MemberService sut;

    @Mock
    private MemberRepository memberRepository;

    @Test
    void 닉네임_중복_여부_확인() {
        // given
        String nickname = "test";
        boolean expected = true;
        given(memberRepository.existsByNickname(nickname)).willReturn(expected);

        // when
        boolean actual = sut.checkNicknameDuplication(nickname);

        // then
        assertThat(actual).isEqualTo(expected);
        then(memberRepository).should().existsByNickname(nickname);
    }

    @Test
    void 이메일_존재_여부_확인() {
        // given
        String email = "test@daum.net";
        boolean expected = true;
        given(memberRepository.existsByEmail(email)).willReturn(expected);

        // when
        boolean actual = sut.existsMemberByEmail(email);

        // then
        assertThat(actual).isEqualTo(expected);
        then(memberRepository).should().existsByEmail(email);
    }

    @Test
    void 주어진_닉네임을_갖는_회원을_조회한다_조회_성공() {
        // given
        String nickname = "test";
        Member expectedMember = createMemberWithoutPhoneNumberAndAddress();
        given(memberRepository.findByNickname(nickname))
                .willReturn(Optional.of(expectedMember));

        // when
        MemberDto dto = sut.findMemberByNickname(nickname);

        // then
        assertAll(
                () -> assertThat(dto.getEmail()).isEqualTo(expectedMember.getEmail()),
                () -> assertThat(dto.getPassword()).isEqualTo(expectedMember.getPassword()),
                () -> assertThat(dto.getAgeRange()).isEqualTo(expectedMember.getAgeRange()),
                () -> assertThat(dto.getGender()).isEqualTo(expectedMember.getGender())
        );
        then(memberRepository).should().findByNickname(nickname);
    }

    @Test
    void 주어진_닉네임을_갖는_회원을_조회한다_조회_실패() {
        // given
        String nickname = "test";
        given(memberRepository.findByNickname(nickname))
                .willReturn(Optional.empty());

        // when
        Throwable t = catchThrowable(() -> sut.findMemberByNickname(nickname));

        // then
        assertThat(t)
                .isInstanceOf(NicknameNotFoundException.class);
        then(memberRepository).should().findByNickname(nickname);
    }

    @Test
    void 주어진_이메일을_갖는_회원을_조회한다_조회_성공() {
        // given
        String email = "test@daum.net";
        Member expectedMember = createMemberWithoutPhoneNumberAndAddress();
        given(memberRepository.findByEmail(email))
                .willReturn(Optional.of(expectedMember));

        // when
        MemberDto dto = sut.findMemberByEmail(email);

        // then
        assertAll(
                () -> assertThat(dto.getEmail()).isEqualTo(expectedMember.getEmail()),
                () -> assertThat(dto.getPassword()).isEqualTo(expectedMember.getPassword()),
                () -> assertThat(dto.getAgeRange()).isEqualTo(expectedMember.getAgeRange()),
                () -> assertThat(dto.getGender()).isEqualTo(expectedMember.getGender())
        );
        then(memberRepository).should().findByEmail(email);
    }

    @Test
    void 주어진_이메일을_갖는_회원을_조회한다_조회_실패() {
        // given
        String email = "test@daum.net";
        given(memberRepository.findByEmail(email))
                .willReturn(Optional.empty());

        // when
        Throwable t = catchThrowable(() -> sut.findMemberByEmail(email));

        // then
        assertThat(t)
                .isInstanceOf(EmailNotFoundException.class);
        then(memberRepository).should().findByEmail(email);
    }

    @Test
    void 주어진_이메일을_갖는_회원의_주소_정보_존재_여부_확인_없는_경우() {
        // given
        String email = "test@daum.net";
        boolean expected = false;
        given(memberRepository.findByEmail(email))
                .willReturn(Optional.of(createMemberWithoutPhoneNumberAndAddress()));

        // when
        boolean actual = sut.checkExistenceMemberAddressByEmail(email);

        // then
        assertThat(actual).isEqualTo(expected);
        then(memberRepository).should().findByEmail(email);
    }

    @Test
    void 주어진_이메일을_갖는_회원의_주소_정보_존재_여부_확인_있는_경우() {
        // given
        String email = "test@daum.net";
        boolean expected = true;
        given(memberRepository.findByEmail(email))
                .willReturn(Optional.of(createMember()));

        // when
        boolean actual = sut.checkExistenceMemberAddressByEmail(email);

        // then
        assertThat(actual).isEqualTo(expected);
        then(memberRepository).should().findByEmail(email);
    }

    @Test
    void 주어진_이메일을_갖는_회원의_폰_번호_존재_여부_확인_있는_경우() {
        // given
        String email = "test@daum.net";
        boolean expected = true;
        given(memberRepository.findByEmail(email))
                .willReturn(Optional.of(createMember()));

        // when
        boolean actual = sut.checkExistenceMemberPhoneNumberByEmail(email);

        // then
        assertThat(actual).isEqualTo(expected);
        then(memberRepository).should().findByEmail(email);
    }

    private MemberDto createMemberDto() {
        return MemberDto.of(
                "test@daum.net",
                "1234",
                20,
                Gender.MALE
        );
    }

    private Member createMember() {
        Member member = createMemberWithoutPhoneNumberAndAddress();
        ReflectionTestUtils.setField(member, "phoneNumber", "010-0000-0000");
        ReflectionTestUtils.setField(member, "address", new Address("서울특별시", "강남구"));

        return member;
    }

    private Member createMemberWithoutPhoneNumberAndAddress() {
        Member member = createMemberDto().toEntity();
        ReflectionTestUtils.setField(member, "id", 1L);

        return member;
    }
}