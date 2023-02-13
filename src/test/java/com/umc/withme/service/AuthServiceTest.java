package com.umc.withme.service;

import com.umc.withme.domain.ImageFile;
import com.umc.withme.domain.Member;
import com.umc.withme.domain.constant.Gender;
import com.umc.withme.dto.member.MemberDto;
import com.umc.withme.repository.ImageFileRepository;
import com.umc.withme.repository.MemberRepository;
import com.umc.withme.security.JwtTokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@DisplayName("[Service] Auth - 로그인/회원가입")
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService sut;

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private ImageFileRepository imageFileRepository;
    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Test
    void 유저_정보가_주어지면_회원으로_등록한다() {
        // given
        MemberDto memberDto = createMemberDto();
        given(imageFileRepository.existsDefaultMemberProfileImage()).willReturn(true);
        given(imageFileRepository.getDefaultMemberProfileImage()).willReturn(createImageFile());
        given(memberRepository.save(any(Member.class))).willReturn(createMember());

        // when
        sut.signUp(memberDto);

        // then
        then(memberRepository).should().save(any(Member.class));
    }

    @Test
    void 이메일이_주어지면_로그인을_수행한다() {
        // given
        String email = "test@naver.com";
        String expected = "token";
        given(jwtTokenProvider.createToken(email)).willReturn(expected);

        // when
        String actual = sut.login(email);

        // then
        assertThat(actual).isEqualTo(expected);
        then(jwtTokenProvider).should().createToken(email);
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
        Member member = createMemberDto().toEntity(createImageFile());
        ReflectionTestUtils.setField(member, "id", 1L);

        return member;
    }

    private ImageFile createImageFile() {
        return ImageFile.builder()
                .fileName("test")
                .storedFileName("test")
                .url("url")
                .build();
    }
}