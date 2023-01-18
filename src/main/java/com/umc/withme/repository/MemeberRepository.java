package com.umc.withme.repository;

import com.umc.withme.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemeberRepository extends JpaRepository<Member, Long> {

    /**
     * 닉네임을 전달받아 DB에 존재하는지 확인한다.
     *
     * @param nickname
     * @return 존재하는 경우 - true, 존재하지 않는 경우 - false
     */
    boolean existsByNickname(String nickname);
}
