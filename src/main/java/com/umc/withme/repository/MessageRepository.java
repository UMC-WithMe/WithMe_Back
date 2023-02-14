package com.umc.withme.repository;

import com.umc.withme.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {

    Long countBySender_IdAndReceiver_IdAndMeet_Id(Long senderId, Long receiverId, Long meetId);

    Message findTopBySender_IdAndReceiver_IdAndMeet_Id(Long senderId, Long receiverId, Long meetId);

    // TODO : 네이티브 쿼리를 추후 JPQL로 변경 필요

    /**
     * 쪽지함 화면에서 보여지는 쪽지들을 조회할 때 사용하는 메소드입니다.
     * 각 쪽지 채팅방의 최신 쪽지 1개씩 가져와 리스트에 담습니다.
     *
     * @param memberId 로그인한 사용자 id
     * @return 쪽지함에 보여질 쪽지들의 리스트를 반환
     */
    @Query(
            value = "select *" +
                    " from (select *" +
                    "      from Message" +
                    "      where (sender_id = :memberId" +
                    "          or receiver_id = :memberId)" +
                    "        and (chatroom_id, meet_id, created_at) in (select chatroom_id, meet_id, max(created_at)" +
                    "                                                   from Message" +
                    "                                                   group by chatroom_id, meet_id)" +
                    "      order by created_at, message_id) my_message" +
                    " group by chatroom_id, meet_id, sender_id, receiver_id, content, message_id, created_at, modified_at, created_by," +
                    "         modified_by" +
                    " order by created_at desc;",
            nativeQuery = true
    )
    List<Message> findAllByMemberId(@Param("memberId") Long memberId);

    List<Message> findAllByChatroom_IdOrderByCreatedAt(Long chatroomId);

    Boolean existsBySender_IdAndReceiver_IdAndMeet_Id(Long senderId, Long receiverId, Long meetId);

    Optional<Message> findFirstByChatroom_Id(Long chatroomId);

    void deleteAllByChatroom_Id(Long chatroomId);
}
