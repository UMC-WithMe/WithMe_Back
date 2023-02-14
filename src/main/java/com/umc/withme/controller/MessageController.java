package com.umc.withme.controller;

import com.umc.withme.dto.common.BaseResponse;
import com.umc.withme.dto.common.DataResponse;
import com.umc.withme.dto.meet.MeetDto;
import com.umc.withme.dto.message.request.MessageCreateRequest;
import com.umc.withme.dto.message.response.MessageInfoResponse;
import com.umc.withme.dto.message.MessageDto;
import com.umc.withme.dto.message.response.MessageInfoListGetResponse;
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
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "쪽지", description = "쪽지 관련 api 입니다.")
public class MessageController {

    private final MessageService messageService;

    @Operation(summary = "쪽지 생성",
            description = "<p>회원이 쪽지 1개를 생성합니다</p>" +
                    "<ul>" +
                    "<li><code>meetId</code> - 관련 모집글</li>" +
                    "<li><<code>receiverId</code> - 받는 회원</li>" +
                    "<li><code>messageCreateRequest</code>의 <code>content</code> - 쪽지 내용</li>" +
                    "</ul>",
            security = @SecurityRequirement(name = "access-token"))
    @PostMapping("/messages")
    public ResponseEntity<BaseResponse> createMessage(
            @Parameter(hidden = true) @AuthenticationPrincipal WithMeAppPrinciple principle,
            @Valid @RequestBody MessageCreateRequest messageCreateRequest,
            @RequestParam Long receiverId,
            @RequestParam Long meetId
    ) {
        messageService.createMessage(principle.getMemberId(), receiverId, meetId, messageCreateRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new BaseResponse(true));
    }

    @Operation(summary = "쪽지함 조회",
            description = "<p> 회원이 자신의 쪽지함을 조회합니다</p>",
            security = @SecurityRequirement(name = "access-token"))
    @GetMapping("/messages")
    public ResponseEntity<DataResponse<List<MessageInfoResponse>>> getMessageList(
            @Parameter(hidden = true) @AuthenticationPrincipal WithMeAppPrinciple principle
    ) {
        List<MessageInfoResponse> messageInfoResponseList = messageService.getMessageList(principle.getMemberId());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new DataResponse<>(messageInfoResponseList));
    }

    @Operation(
            summary = "특정 쪽지방의 쪽지 조회",
            description = "<p>채팅방의 쪽지 리스트를를 조회합니다. <code>chatroomId</code> - 쪽지 채팅방 id </p>",
            security = @SecurityRequirement(name = "access-token")
    )
    @GetMapping("/messages/{chatroomId}")
    public ResponseEntity<DataResponse<MessageInfoListGetResponse>> getMessage(
            @PathVariable Long chatroomId,
            @Parameter(hidden = true) @AuthenticationPrincipal WithMeAppPrinciple principle
    ) {
        List<MessageDto> messageDtos = messageService.findMessages(chatroomId, principle.getMemberId());
        MeetDto meetDto = messageService.findMeetByChatroomId(chatroomId);

        MessageInfoListGetResponse response = MessageInfoListGetResponse.from(
                meetDto.getMeetId(),
                meetDto.getTitle(),
                messageDtos
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new DataResponse<>(response));
    }
}
