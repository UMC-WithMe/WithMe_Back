package com.umc.withme.repository;

import com.umc.withme.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query(
            value = "select *\n" +
                    " from (select *\n" +
                    "      from message\n" +
                    "      where (sender_id = :memberId\n" +
                    "          or receiver_id = :memberId)\n" +
                    "        and (meet_id, created_at) in (select meet_id, max(created_at)\n" +
                    "                                      from message\n" +
                    "                                      group by meet_id)\n" +
                    "      order by created_at, message_id) my_message\n" +
                    " group by meet_id, content, message_id, created_at, modified_at, created_by, modified_by,\n" +
                    "         receiver_id, sender_id\n" +
                    " order by created_at desc",
            nativeQuery = true
    )
    List<Message> findAllByMemberId(@Param("memberId") Long memberId);
}
