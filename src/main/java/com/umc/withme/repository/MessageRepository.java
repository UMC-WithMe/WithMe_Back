package com.umc.withme.repository;

import com.umc.withme.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllByChatroom_IdOrderByCreatedAt(Long chatroomId);

    Long countBySender_IdAndReceiver_IdAndMeet_Id(Long senderId, Long receiverId, Long meetId);

    Message findTopBySender_IdAndReceiver_IdAndMeet_Id(Long receiverId, Long senderId, Long meetId);
}
