package com.umc.withme.controller;

import com.umc.withme.dto.common.DataResponse;
import com.umc.withme.dto.message.MessageCreateRequest;
import com.umc.withme.dto.message.MessageCreateResponse;
import com.umc.withme.security.WithMeAppPrinciple;
import com.umc.withme.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MessageController {

    private final MessageService messageService;

    @PostMapping("/messages")
    public ResponseEntity<DataResponse<MessageCreateResponse>> createMessage(
            @Valid@RequestBody MessageCreateRequest messageCreateRequest,
            @AuthenticationPrincipal WithMeAppPrinciple principle, @RequestParam Long receiverId,
            @RequestParam Long meetId){

        Long messageId = messageService.createMessage(principle.getMemberId(), receiverId, meetId, messageCreateRequest);
        MessageCreateResponse messageCreateResponse = MessageCreateResponse.of(messageId);

        return ResponseEntity.status(HttpStatus.CREATED).body(new DataResponse<>(messageCreateResponse));
    }
}
