package com.umc.withme.service;

import com.umc.withme.domain.ImageFile;
import com.umc.withme.dto.member.MemberDto;
import com.umc.withme.repository.ImageFileRepository;
import com.umc.withme.repository.MemberRepository;
import com.umc.withme.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AuthService {

    private final S3FileService s3FileService;
    private final MemberRepository memberRepository;
    private final ImageFileRepository imageFileRepository;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 회원 정보({@link MemberDto})를 입력받아 DB에 회원으로 등록한다.
     *
     * @param memberDto 등록할 회원 정보
     */
    @Transactional
    public void signUp(MemberDto memberDto) {
        ImageFile defaultImageFile;
        if (imageFileRepository.existsDefaultMemberProfileImage()) {
            defaultImageFile = imageFileRepository.getDefaultMemberProfileImage();
        } else {
            defaultImageFile = s3FileService.saveMemberDefaultImage();
        }
        memberRepository.save(memberDto.toEntity(defaultImageFile));
    }

    /**
     * 사용자의 email을 전달받아 적절히 JWT token을 생성하여 반환한다.
     *
     * @param email 로그인하려는 회원의 email
     * @return 생성된 JWT token
     */
    public String login(String email) {
        return jwtTokenProvider.createToken(email);
    }
}
