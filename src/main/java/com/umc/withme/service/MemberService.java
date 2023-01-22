package com.umc.withme.service;

import com.umc.withme.dto.member.MemberDto;
import com.umc.withme.exception.member.EmailNotFoundException;
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
     * 특정 email을 갖는 회원의 존재 여부를 반환한다.
     *
     * @param email 존재 여부를 확인할 회원의 email
     * @return 해당 email을 갖는 회원이 존재하면 true, 존재하지 않으면 false를 return
     */
    public boolean existsMemberByEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    /**
     * 특정 닉네임을 가진 회원을 조회한 후 반환한다.
     *
     * @param nickname
     * @return {@link MemberDto}에 회원 정보를 담아 반환
     * @throws NicknameNotFoundException 닉네임이 존재하지 않을 경우
     */
    public MemberDto findMemberByNickname(String nickname) {
        return memberRepository.findByNickname(nickname)
                .map(MemberDto::from)
                .orElseThrow(NicknameNotFoundException::new);
    }

    /**
     * 특정 email을 갖는 회원을 조회한다.
     *
     * @param email 조회할 회원의 email
     * @return  조회된 회원의 정보를 {@link MemberDto}에 담아서 return
     * @throws EmailNotFoundException   해당 email을 갖는 회원이 존재하지 않는 경우
     */
    public MemberDto findMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .map(MemberDto::from)
                .orElseThrow(EmailNotFoundException::new);
    }

    /**
     * 특정 email을 갖는 회원의 주소(address) 정보가 있는지 확인한다.
     *
     * @param email 확인하려는 회원의 email
     * @return  해당 email을 갖는 회원의 주소(address) 정보가 있는 경우 true를, 없다면 false를 return
     */
    public boolean checkExistenceMemberAddressByEmail(String email) {
        return findMemberByEmail(email).getAddress() != null;
    }

    /**
     * 특정 email을 갖는 회원의 폰 번호 정보가 있는지 확인한다.
     *
     * @param email 확인하려는 회원의 email
     * @return  해당 email을 갖는 회원의 폰 번호 정보가 있는 경우 true를, 없다면 false를 return
     */
    public boolean checkExistenceMemberPhoneNumberByEmail(String email) {
        return findMemberByEmail(email).getPhoneNumber() != null;
    }
}
