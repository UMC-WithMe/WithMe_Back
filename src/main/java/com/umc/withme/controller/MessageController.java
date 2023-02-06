package com.umc.withme.controller;

import com.umc.withme.dto.common.DataResponse;
import com.umc.withme.dto.message.MessageCreateRequest;
import com.umc.withme.dto.message.MessageCreateResponse;
import com.umc.withme.dto.message.MessageInfoResponse;
import com.umc.withme.security.WithMeAppPrinciple;
import com.umc.withme.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "쪽지", description = "쪽지 관련 api 입니다.")
public class MessageController {

    private final MessageService messageService;

    @Operation(summary = "쪽지 생성",
               description = "<p> 회원이 쪽지 1개를 생성합니다:: <code>meetId</code> - 관련 모집글, <code>receiverId</code> - 받는 회원, " +
                             "<code>messageCreateRequest</code>의 <code>content</code></p> - 쪽지 내용" )
    @PostMapping("/messages")
    public ResponseEntity<DataResponse<MessageCreateResponse>> createMessage(
            @Valid@RequestBody MessageCreateRequest messageCreateRequest,
            @AuthenticationPrincipal WithMeAppPrinciple principle, @RequestParam Long receiverId,
            @RequestParam Long meetId){

        Long messageId = messageService.createMessage(principle.getMemberId(), receiverId, meetId, messageCreateRequest);
        MessageCreateResponse messageCreateResponse = MessageCreateResponse.of(messageId);

        return ResponseEntity.status(HttpStatus.CREATED).body(new DataResponse<>(messageCreateResponse));
    }
    
    @Operation(summary = "쪽지함 조회", description = "<p> 회원이 자신의 쪽지함을 조회합니다</p>")
    @GetMapping("/messages")
    public ResponseEntity<DataResponse<List<MessageInfoResponse>>> getMessageList(
            @Parameter(hidden = true) @AuthenticationPrincipal WithMeAppPrinciple principle){

        List<MessageInfoResponse> messageInfoResponseList = messageService.getMessageList(principle.getMemberId());

        return ResponseEntity.status(HttpStatus.OK).body(new DataResponse<>(messageInfoResponseList));
    }
}
