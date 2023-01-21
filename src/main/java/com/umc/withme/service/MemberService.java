package com.umc.withme.service;

import com.umc.withme.dto.member.MemberDto;
import com.umc.withme.exception.member.NicknameNotFoundException;
import com.umc.withme.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * DB에서 닉네임 중복 여부 확인 후 결과를 반환한다.
     *
     * @param nickname
     * @return 중복인 경우 - true, 중복이 아닌 경우 - false
     */
    public boolean checkNicknameDuplication(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }

    /**
     * 특정 닉네임을 가진 회원 정보를 컨트롤러에게 반환한다.
     *
     * @param nickname
     * @return {@link MemberDto}에 회원 정보를 담아 반환
     * @throws NicknameNotFoundException 닉네임이 존재하지 않을 경우
     */
    public MemberDto getMemberInfo(String nickname) {
        return memberRepository.findByNickname(nickname)
                .map(MemberDto::from)
                .orElseThrow(NicknameNotFoundException::new);
    }
}
