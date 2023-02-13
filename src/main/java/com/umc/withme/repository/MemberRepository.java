package com.umc.withme.repository;

import com.umc.withme.domain.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    /**
     * 닉네임을 전달받아 DB에 존재하는지 확인한다.
     *
     * @param nickname
     * @return 존재하는 경우 - true, 존재하지 않는 경우 - false
     */
    boolean existsByNickname(String nickname);

    boolean existsByEmail(String email);

    /**
     * 해당 닉네임을 가진 Member 객체를 조회한다.
     *
     * @param nickname
     * @return Member 객체
     */
    @EntityGraph(attributePaths = {"profileImage", "address"})
    Optional<Member> findByNickname(String nickname);

    @EntityGraph(attributePaths = {"profileImage", "address"})
    Optional<Member> findByEmail(String email);
}
