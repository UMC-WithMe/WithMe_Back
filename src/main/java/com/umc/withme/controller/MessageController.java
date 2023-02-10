package com.umc.withme.controller;

import com.umc.withme.dto.common.BaseResponse;
import com.umc.withme.dto.message.MessageCreateRequest;
import com.umc.withme.dto.message.MessageCreateResponse;
import com.umc.withme.security.WithMeAppPrinciple;
import com.umc.withme.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "쪽지", description = "쪽지 관련 api 입니다.")
public class MessageController {

    private final MessageService messageService;

    /**
     * 쪽지 생성 API
     *
     * @param messageCreateRequest 쪽지 생성 요청 데이터 (쪽지 내용)
     * @param principle
     * @param receiverId 쪽지 받는 회원 id(pk)
     * @param meetId 관련 모집글 id(pk)
     * @return 쪽지생성 성공 여부
     */

    @Operation(summary = "쪽지 생성",
            description = "<p> 회원이 쪽지 1개를 생성합니다:: <code>meetId</code> - 관련 모집글, <code>receiverId</code> - 받는 회원, " +
                    "<code>messageCreateRequest</code>의 <code>content</code></p> - 쪽지 내용",
            security = @SecurityRequirement(name = "access-token"))
    @PostMapping("/messages")
    public ResponseEntity<BaseResponse> createMessage(
            @Valid @RequestBody MessageCreateRequest messageCreateRequest,
            @Parameter(hidden = true) @AuthenticationPrincipal WithMeAppPrinciple principle,
            @RequestParam Long receiverId,
            @RequestParam Long meetId) {

        Long messageId = messageService.createMessage(principle.getMemberId(), receiverId, meetId, messageCreateRequest);
        MessageCreateResponse messageCreateResponse = MessageCreateResponse.of(messageId);

        return ResponseEntity.status(HttpStatus.CREATED).body(new BaseResponse(true));
    }
}
