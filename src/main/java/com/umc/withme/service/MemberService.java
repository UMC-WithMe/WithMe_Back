package com.umc.withme.service;

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
}
