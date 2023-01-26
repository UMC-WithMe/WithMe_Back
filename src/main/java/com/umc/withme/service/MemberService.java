package com.umc.withme.service;

import com.umc.withme.domain.Address;
import com.umc.withme.domain.Member;
import com.umc.withme.dto.member.MemberDto;
import com.umc.withme.exception.address.AddressNotFoundException;
import com.umc.withme.exception.member.EmailNotFoundException;
import com.umc.withme.exception.member.NicknameNotFoundException;
import com.umc.withme.repository.AddressRepository;
import com.umc.withme.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final AddressRepository addressRepository;

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
        return MemberDto.from(getMemberByEmail(email));
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

    /**
     * memberId에 해당하는 회원 entity의 폰 번호를 phoneNumber로 설정한다.
     *
     * @param email 폰 번호를 설정할 회원의 email
     * @param phoneNumber   설정할 폰 번호
     */
    @Transactional
    public void updateMemberPhoneNumber(String email, String phoneNumber) {
        Member member = getMemberByEmail(email);
        member.setPhoneNumber(phoneNumber);
    }

    /**
     * email에 해당하는 회원 entity의 주소 정보를 sido, sgg와 일치하는 주소로 설정한다.
     *
     * @param email 주소를 설정할 회원의 email
     * @param sido  시/도
     * @param sgg   시/군/구
     */
    @Transactional
    public void updateMemberAddress(String email, String sido, String sgg) {
        Member member = getMemberByEmail(email);
        Address address = addressRepository.findBySidoAndSgg(sido, sgg)
                .orElseThrow(() -> new AddressNotFoundException(sido, sgg));
        member.setAddress(address);
    }

    /**
     * email에 해당하는 회원 entity를 조회하여 반환한다.
     *
     * @param email 조회할 회원의 email
     * @return  조회한 회원 entity 객체
     */
    private Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(EmailNotFoundException::new);
    }
}
