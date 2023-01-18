package com.umc.withme.service;

import com.umc.withme.domain.Member;
import com.umc.withme.dto.member.MemberInfoGetResponse;
import com.umc.withme.exception.member.NicknameNotFoundException;
import com.umc.withme.repository.MemeberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemeberRepository memeberRepository;

    /**
     * DB에서 닉네임 중복여부 확인 후 결과를 컨트롤러에게 반환한다.
     *
     * @param nickname
     * @return 중복인 경우 - true, 중복이 아닌 경우 - false
     */
    public boolean checkNicknameDuplication(String nickname) {
        return memeberRepository.existsByNickname(nickname);
    }

    /**
     * 특정 닉네임을 가진 회원 정보를 컨트롤러에게 반환한다.
     *
     * @param nickname
     * @return 회원 정보를 MemberInfoGetResponse 객체로 바꾸어 반환
     * @throws NicknameNotFoundException 닉네임이 존재하지 않을 경우
     */
    public MemberInfoGetResponse getMemberInfo(String nickname) {
        Member findMember = memeberRepository.findByNickname(nickname)
                .orElseThrow(() -> new NicknameNotFoundException());
        return MemberInfoGetResponse.from(findMember);
    }
}
