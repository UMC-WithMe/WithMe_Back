package com.umc.withme.controller;

import com.umc.withme.dto.common.BaseResponse;
import com.umc.withme.dto.common.DataResponse;
import com.umc.withme.dto.meet.MeetCreateRequest;
import com.umc.withme.dto.meet.MeetCreateResponse;
import com.umc.withme.dto.meet.MeetDto;
import com.umc.withme.dto.meet.MeetInfoGetResponse;
import com.umc.withme.security.WithMeAppPrinciple;
import com.umc.withme.service.MeetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "MeetController", description = "모임 API Controller 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MeetController {

    private final MeetService meetService;

    /**
     * 모임글 생성 API
     *
     * @param meetCreateRequest 생성하려는 모임 모집글 form 데이터
     * @return 생성된 모임글 id를 data 에 담아서 반환한다.
     */
    @Operation(
            summary = "모임 모집글 생성",
            description = "<p>request body 에 입력된 정보를 바탕으로 모임 모집글을 1개 생성합니다.</p>",
            security = @SecurityRequirement(name = "access-token")
    )
    @PostMapping("/meets")
    public ResponseEntity<DataResponse<MeetCreateResponse>> createMeet(
            @Valid @RequestBody MeetCreateRequest meetCreateRequest,
            @Parameter(hidden = true) @AuthenticationPrincipal WithMeAppPrinciple principle
    ) {
        Long meetId = meetService.createMeet(meetCreateRequest.toDto(), principle.getUsername());

        MeetCreateResponse response = MeetCreateResponse.of(meetId);

        return new ResponseEntity<>(
                new DataResponse<>(response),
                HttpStatus.OK
        );
    }

    /**
     * 모임 단건 조회 API
     * 모임 id로 모임을 1건 조회한다.
     *
     * @param meetId 조회하려는 모임의 id
     * @return 조회한 모임의 정보를 담은 MeetInfoGetResponse를 data에 담아서 반환한다.
     */
    @Operation(
            summary = "모임 모집글 1개 조회 API",
            description = "<p><code>meetId</code>에 해당하는 모임의 정보를 response body 에 넣어 전달합니다.</p>",
            security = @SecurityRequirement(name = "access-token")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "404", description = "2400: <code>meetId</code>에 해당하는 모임이 없는 경우", content = @Content)
    })
    @GetMapping("/meets/{meetId}")
    public ResponseEntity<DataResponse<MeetInfoGetResponse>> getMeet(@PathVariable Long meetId) {
        MeetDto meetDto = meetService.findById(meetId);

        MeetInfoGetResponse response = MeetInfoGetResponse.from(meetDto);

        return new ResponseEntity<>(
                new DataResponse<>(response),
                HttpStatus.OK
        );
    }

    /**
     * 모임글 단건 삭제 API
     * 모임의 id를 입력받아 해당하는 모임이 있으면 삭제한다.
     *
     * @param meetId 삭제하려는 모임의 id
     * @return 삭제한 모임의 id를 데이터에 담아서 반환한다.
     */
    @Operation(
            summary = "모임 모집글 1개 삭제 API",
            description = "<p><code>meetId</code>에 해당하는 모임을 삭제하고 삭제한 <code>meetId</code>를 response body에 넣어 전달합니다.</p>",
            security = @SecurityRequirement(name = "access-token")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "404", description = "2400: <code>meetId</code>에 해당하는 모임이 없는 경우", content = @Content)
    })
    @DeleteMapping("/meets/{meetId}")
    public ResponseEntity<BaseResponse> deleteMeet(
            @PathVariable Long meetId,
            @Parameter(hidden = true) @AuthenticationPrincipal WithMeAppPrinciple principle
    ) {
        meetService.deleteMeetById(meetId, principle.getUsername());

        return new ResponseEntity<>(
                new BaseResponse(true),
                HttpStatus.OK
        );
    }
}