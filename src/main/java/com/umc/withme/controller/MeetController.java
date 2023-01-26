package com.umc.withme.controller;

import com.umc.withme.dto.common.DataResponse;
import com.umc.withme.dto.meet.MeetCreateResponse;
import com.umc.withme.dto.meet.MeetDto;
import com.umc.withme.dto.meet.MeetFormRequest;
import com.umc.withme.dto.meet.MeetInfoGetResponse;
import com.umc.withme.security.WithMeAppPrinciple;
import com.umc.withme.service.MeetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MeetController {

    private final MeetService meetService;

    /**
     * 모임글 생성 API
     *
     * @param meetCreateRequest
     * @return 생성된 모임글 id를 data 에 담아서 반환한다.
     */
    @Operation(
            summary = "모임 모집글 생성",
            description = "<p>request body 에 입력된 정보를 바탕으로 모임 모집글을 1개 생성합니다.</p>",
            security = @SecurityRequirement(name = "access-token")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "404", description = "1401: <code>email</code>에 해당하는 회원이 없는 경우", content = @Content)
    })
    @PostMapping("/meet")
    public ResponseEntity<DataResponse<MeetCreateResponse>> createMeet(
            @Valid @RequestBody MeetFormRequest meetFormRequest,
            @Parameter(hidden = true) @AuthenticationPrincipal WithMeAppPrinciple principle
    ) {
        Long meetId = meetService.createMeet(meetFormRequest.toDto(), principle.getUsername());

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
            description = "<p><code>meetId</code>에 해당하는 모임의의 정보를 request body 에 넣어 전달합니다.</p>",
            security = @SecurityRequirement(name = "access-token")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "404", description = "1401: <code>email</code>에 해당하는 회원이 없는 경우", content = @Content)
    })
    @GetMapping("/meet/{meetId}")
    public ResponseEntity<DataResponse<MeetInfoGetResponse>> getMeet(@PathVariable Long meetId) {
        MeetDto meetDto = meetService.findById(meetId);

        MeetInfoGetResponse response = MeetInfoGetResponse.from(meetDto);

        return new ResponseEntity<>(
                new DataResponse<>(response),
                HttpStatus.OK
        );
    }

    /**
     * 모임 모집글 수정 API
     *
     * @param meetId          수정하려는 모집글의 id
     * @param meetFormRequest 수정하려는 정보가 담긴 요청 DTO
     * @param principle       사용자 정보
     * @return 수정된 모임 모집글의 정보를 담은 MeetInfoGetResponse를 data에 담아서 반환한다.
     */
    @Operation(
            summary = "모임 모집글 수정 API",
            description = "<p><code>meetId</code>에 해당하는 모임을 <code>request body</code>에 담긴 정보로 수정하고" +
                    "수정된 모임을 response body 에 넣어 전달합니다.</p>",
            security = @SecurityRequirement(name = "access-token")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "404", description = "1401: <code>email</code>에 해당하는 회원이 없는 경우", content = @Content)
    })
    @PutMapping("/meet/{meetId}")
    public ResponseEntity<DataResponse<MeetInfoGetResponse>> updateMeet(
            @PathVariable Long meetId,
            @Valid @RequestBody MeetFormRequest meetFormRequest,
            @Parameter(hidden = true) @AuthenticationPrincipal WithMeAppPrinciple principle) {
        MeetDto meetDto = meetService.updateById(meetId, principle.getUsername(), meetFormRequest.toDto());

        MeetInfoGetResponse response = MeetInfoGetResponse.from(meetDto);

        return new ResponseEntity<>(
                new DataResponse<>(response),
                HttpStatus.OK
        );
    }
}
