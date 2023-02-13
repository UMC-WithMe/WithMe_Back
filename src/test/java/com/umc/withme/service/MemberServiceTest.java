package com.umc.withme.service;

import com.umc.withme.domain.Address;
import com.umc.withme.domain.ImageFile;
import com.umc.withme.domain.Member;
import com.umc.withme.domain.constant.Gender;
import com.umc.withme.dto.member.MemberDto;
import com.umc.withme.exception.member.EmailNotFoundException;
import com.umc.withme.exception.member.NicknameDuplicateException;
import com.umc.withme.exception.member.NicknameNotFoundException;
import com.umc.withme.repository.AddressRepository;
import com.umc.withme.repository.ImageFileRepository;
import com.umc.withme.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@DisplayName("[Service] Member")
@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    private static final String TEST_EMAIL = "test@daum.net";
    private static final String TEST_PASSWORD = "1234";
    private static final String TEST_NICKNAME = "Test";

    @InjectMocks
    private MemberService sut;

    @Mock
    private S3FileService s3FileService;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private AddressRepository addressRepository;
    @Mock
    private ImageFileRepository imageFileRepository;

    @Test
    void 닉네임_중복_여부_확인() {
        // given
        boolean expected = true;
        given(memberRepository.existsByNickname(MemberServiceTest.TEST_NICKNAME)).willReturn(expected);

        // when
        boolean actual = sut.checkNicknameDuplication(MemberServiceTest.TEST_NICKNAME);

        // then
        assertThat(actual).isEqualTo(expected);
        then(memberRepository).should().existsByNickname(MemberServiceTest.TEST_NICKNAME);
    }

    @Test
    void 이메일_존재_여부_확인() {
        // given
        boolean expected = true;
        given(memberRepository.existsByEmail(TEST_EMAIL)).willReturn(expected);

        // when
        boolean actual = sut.existsMemberByEmail(TEST_EMAIL);

        // then
        assertThat(actual).isEqualTo(expected);
        then(memberRepository).should().existsByEmail(TEST_EMAIL);
    }

    @Test
    void 주어진_닉네임을_갖는_회원을_조회한다_조회_성공() {
        // given
        Member expectedMember = createMemberWithoutPhoneNumberAndAddress();
        given(memberRepository.findByNickname(MemberServiceTest.TEST_NICKNAME))
                .willReturn(Optional.of(expectedMember));

        // when
        MemberDto dto = sut.findMemberByNickname(MemberServiceTest.TEST_NICKNAME);

        // then
        assertAll(
                () -> assertThat(dto.getEmail()).isEqualTo(expectedMember.getEmail()),
                () -> assertThat(dto.getPassword()).isEqualTo(expectedMember.getPassword()),
                () -> assertThat(dto.getAgeRange()).isEqualTo(expectedMember.getAgeRange()),
                () -> assertThat(dto.getGender()).isEqualTo(expectedMember.getGender())
        );
        then(memberRepository).should().findByNickname(MemberServiceTest.TEST_NICKNAME);
    }

    @Test
    void 주어진_닉네임을_갖는_회원을_조회한다_조회_실패() {
        // given
        given(memberRepository.findByNickname(MemberServiceTest.TEST_NICKNAME))
                .willReturn(Optional.empty());

        // when
        Throwable t = catchThrowable(() -> sut.findMemberByNickname(MemberServiceTest.TEST_NICKNAME));

        // then
        assertThat(t)
                .isInstanceOf(NicknameNotFoundException.class);
        then(memberRepository).should().findByNickname(MemberServiceTest.TEST_NICKNAME);
    }

    @Test
    void 주어진_이메일을_갖는_회원을_조회한다_조회_성공() {
        // given
        Member expectedMember = createMemberWithoutPhoneNumberAndAddress();
        given(memberRepository.findByEmail(TEST_EMAIL))
                .willReturn(Optional.of(expectedMember));

        // when
        MemberDto dto = sut.findMemberByEmail(TEST_EMAIL);

        // then
        assertAll(
                () -> assertThat(dto.getEmail()).isEqualTo(expectedMember.getEmail()),
                () -> assertThat(dto.getPassword()).isEqualTo(expectedMember.getPassword()),
                () -> assertThat(dto.getAgeRange()).isEqualTo(expectedMember.getAgeRange()),
                () -> assertThat(dto.getGender()).isEqualTo(expectedMember.getGender())
        );
        then(memberRepository).should().findByEmail(TEST_EMAIL);
    }

    @Test
    void 주어진_이메일을_갖는_회원을_조회한다_조회_실패() {
        // given
        given(memberRepository.findByEmail(TEST_EMAIL))
                .willReturn(Optional.empty());

        // when
        Throwable t = catchThrowable(() -> sut.findMemberByEmail(TEST_EMAIL));

        // then
        assertThat(t)
                .isInstanceOf(EmailNotFoundException.class);
        then(memberRepository).should().findByEmail(TEST_EMAIL);
    }

    @Test
    void 주어진_이메일을_갖는_회원의_주소_정보_존재_여부_확인_없는_경우() {
        // given
        boolean expected = false;
        given(memberRepository.findByEmail(TEST_EMAIL))
                .willReturn(Optional.of(createMemberWithoutPhoneNumberAndAddress()));

        // when
        boolean actual = sut.checkExistenceMemberAddressByEmail(TEST_EMAIL);

        // then
        assertThat(actual).isEqualTo(expected);
        then(memberRepository).should().findByEmail(TEST_EMAIL);
    }

    @Test
    void 주어진_이메일을_갖는_회원의_주소_정보_존재_여부_확인_있는_경우() {
        // given
        boolean expected = true;
        given(memberRepository.findByEmail(TEST_EMAIL))
                .willReturn(Optional.of(createMember()));

        // when
        boolean actual = sut.checkExistenceMemberAddressByEmail(TEST_EMAIL);

        // then
        assertThat(actual).isEqualTo(expected);
        then(memberRepository).should().findByEmail(TEST_EMAIL);
    }

    @Test
    void 주어진_이메일을_갖는_회원의_폰_번호_존재_여부_확인_있는_경우() {
        // given
        boolean expected = true;
        given(memberRepository.findByEmail(TEST_EMAIL))
                .willReturn(Optional.of(createMember()));

        // when
        boolean actual = sut.checkExistenceMemberPhoneNumberByEmail(TEST_EMAIL);

        // then
        assertThat(actual).isEqualTo(expected);
        then(memberRepository).should().findByEmail(TEST_EMAIL);
    }

    @Test
    void 회원_이메일과_폰_번호가_주어지면_회원의_폰_번호를_업데이트한다() {
        // given
        Member member = createMember();
        String newPhoneNumber = "010-1234-5678";
        given(memberRepository.findByEmail(TEST_EMAIL)).willReturn(Optional.of(member));

        // when
        sut.updateMemberPhoneNumber(TEST_EMAIL, newPhoneNumber);

        // then
        assertThat(member)
                .hasFieldOrPropertyWithValue("phoneNumber", newPhoneNumber);
        then(memberRepository).should().findByEmail(TEST_EMAIL);
    }

    @Test
    void 회원_이메일과_닉네임이_주어지면_회원의_닉네임을_업데이트한다_정상호출() {
        // given
        Member member = createMember();
        String newNickname = "new";
        given(memberRepository.existsByNickname(newNickname)).willReturn(false);
        given(memberRepository.findByEmail(TEST_EMAIL)).willReturn(Optional.of(member));

        // when
        sut.updateMemberNickname(TEST_EMAIL, newNickname);

        // then
        assertThat(member)
                .hasFieldOrPropertyWithValue("nickname", newNickname);
        then(memberRepository).should().existsByNickname(newNickname);
        then(memberRepository).should().findByEmail(TEST_EMAIL);
    }

    @Test
    void 회원_이메일과_닉네임이_주어지면_회원의_닉네임을_업데이트한다_닉네임_중복() {
        // given
        Member member = createMember();
        String newNickname = "new";
        given(memberRepository.existsByNickname(newNickname)).willReturn(true);

        // when
        Throwable t = catchThrowable(() -> sut.updateMemberNickname(TEST_EMAIL, newNickname));

        // then
        assertThat(t).isInstanceOf(NicknameDuplicateException.class);
        then(memberRepository).should().existsByNickname(newNickname);
        then(memberRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void 회원_이메일과_주소_정보가_주어지면_회원의_주소_정보를_업데이트한다() {
        // given
        Member member = createMember();
        String sido = "제주특별자치도";
        String sgg = "제주시";
        given(memberRepository.findByEmail(TEST_EMAIL))
                .willReturn(Optional.of(member));
        given(addressRepository.findBySidoAndSgg(sido, sgg))
                .willReturn(Optional.of(createAddress(sido, sgg)));

        // when
        sut.updateMemberAddress(TEST_EMAIL, sido, sgg);

        // then
        assertAll(
                () -> assertThat(member.getAddress().getSido()).isEqualTo(sido),
                () -> assertThat(member.getAddress().getSgg()).isEqualTo(sgg)
        );
        then(memberRepository).should().findByEmail(TEST_EMAIL);
        then(addressRepository).should().findBySidoAndSgg(sido, sgg);
    }

    @Test
    void 회원_이메일과_이미지_파일이_주어지면_회원의_프로필_이미지_파일을_업데이트한다() throws IOException {
        // given
        Member member = createMember();
        MultipartFile imageForUpdate = new MockMultipartFile(
                "file",
                "test.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "test".getBytes()
        );
        ImageFile expected = createImageFileWithId(imageForUpdate.getOriginalFilename());
        given(memberRepository.findByEmail(TEST_EMAIL)).willReturn(Optional.of(member));
        given(s3FileService.saveFile(imageForUpdate)).willReturn(expected);

        // when
        sut.updateMemberProfileImage(TEST_EMAIL, imageForUpdate);

        // then
        assertThat(member.getProfileImage().getFileName()).isEqualTo(imageForUpdate.getOriginalFilename());
        then(memberRepository).should().findByEmail(TEST_EMAIL);
        then(s3FileService).should().saveFile(imageForUpdate);
    }

    private MemberDto createMemberDto() {
        return MemberDto.of(
                TEST_EMAIL,
                TEST_PASSWORD,
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
        Member member = createMemberDto().toEntity(createImageFile());
        ReflectionTestUtils.setField(member, "id", 1L);

        return member;
    }

    private Address createAddress(String sido, String sgg) {
        Address address = new Address(sido, sgg);
        ReflectionTestUtils.setField(address, "id", 1L);

        return address;
    }

    private ImageFile createImageFile(String fileName) {
        return ImageFile.builder()
                .fileName(fileName)
                .storedFileName("test")
                .url("url")
                .build();
    }

    private ImageFile createImageFile() {
        return createImageFile("test");
    }

    private ImageFile createImageFileWithId(String fileName) {
        ImageFile imageFile = createImageFile(fileName);
        ReflectionTestUtils.setField(imageFile, "id", 1L);
        return imageFile;
    }

    private ImageFile createImageFileWithId() {
        return createImageFileWithId("test");
    }
}