package com.umc.withme.repository;

import com.umc.withme.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    /**
     * 모임과 쪽지 작성자가 같은 쪽지들 중 가장 마지막 쪽지들을 리스트로 조회해온다.
     * 이때 쪽지의 순서는 최신이다.
     *
     * @param memberId 쪽지함을 조회하려는 사용자 id (현재 로그인한 사용자 id)
     * @return 각 쪽지방에서 가장 최신 쪽지들의 리스트
     */
    @Query(
            value = "select *\n" +
                    " from (select *\n" +
                    "      from Message\n" +
                    "      where (sender_id = :memberId\n" +
                    "          or receiver_id = :memberId)\n" +
                    "        and (meet_id, sender_id, created_at) in (select meet_id, sender_id, max(created_at)\n" +
                    "                                      from Message\n" +
                    "                                      group by meet_id, sender_id)\n" +
                    "      order by created_at, message_id) my_message\n" +
                    " group by meet_id, sender_id, receiver_id, content, message_id, created_at, modified_at, created_by, modified_by\n" +
                    " order by created_at desc",
            nativeQuery = true
    )
    List<Message> findAllByMemberId(@Param("memberId") Long memberId);
}
