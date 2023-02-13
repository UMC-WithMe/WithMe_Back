package com.umc.withme.controller;

import com.umc.withme.domain.constant.MeetCategory;
import com.umc.withme.domain.constant.MeetStatus;
import com.umc.withme.dto.common.BaseResponse;
import com.umc.withme.dto.common.DataResponse;
import com.umc.withme.dto.meet.MeetCreateResponse;
import com.umc.withme.dto.meet.MeetDto;
import com.umc.withme.dto.meet.MeetFormRequest;
import com.umc.withme.dto.meet.MeetInfoResponse;
import com.umc.withme.dto.meet.*;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.List;

@Tag(name = "모임/모집 글", description = "모임과 모집 글 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api")
public class MeetController {

    private final MeetService meetService;

    @Operation(
            summary = "모임 모집글 생성",
            description = "<p>request body 에 입력된 정보를 바탕으로 모임 모집글을 1개 생성합니다.</p>",
            security = @SecurityRequirement(name = "access-token")
    )
    @PostMapping("/meets")
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
    public ResponseEntity<DataResponse<MeetInfoResponse>> getMeet(@PathVariable Long meetId) {
        MeetDto meetDto = meetService.findById(meetId);

        MeetInfoResponse response = MeetInfoResponse.from(meetDto);

        return new ResponseEntity<>(
                new DataResponse<>(response),
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "모임 모집글 수정 API",
            description = "<p><code>meetId</code>에 해당하는 모임을 <code>request body</code>에 담긴 정보로 수정하고" +
                    "수정된 모임을 response body 에 넣어 전달합니다.</p>",
            security = @SecurityRequirement(name = "access-token")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "404", description = "2400: <code>meetId</code>에 해당하는 모임이 없는 경우", content = @Content)
    })
    @PutMapping("/meets/{meetId}")
    public ResponseEntity<DataResponse<MeetInfoResponse>> updateMeet(
            @PathVariable Long meetId,
            @Valid @RequestBody MeetFormRequest meetFormRequest,
            @Parameter(hidden = true) @AuthenticationPrincipal WithMeAppPrinciple principle) {
        MeetDto meetDto = meetService.updateById(meetId, principle.getMemberId(), meetFormRequest.toDto());

        MeetInfoResponse response = MeetInfoResponse.from(meetDto);

        return new ResponseEntity<>(
                new DataResponse<>(response),
                HttpStatus.OK
        );
    }

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
        meetService.deleteMeetById(meetId, principle.getMemberId());

        return new ResponseEntity<>(
                new BaseResponse(true),
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "모임 모집글 리스트 조회 API",
            description = "<p>조건에 맞는 모임 모집글 목록들을 반환합니다." +
                    "<p><code>category</code>: null일 때 모든 카테고리에 대한 모집글이 조회됩니다.</p>" +
                    "<p><code>isLocal</code>: 내동네 모집글 조회일 때 true 아니면 false로 하면 됩니다.</p>" +
                    "<p><code>title</code>: 제목 검색 조건이 없을 때는 null로 하면 됩니다.</p></p>",
            security = @SecurityRequirement(name = "access-token")
    )
    @GetMapping("/meets")
    public ResponseEntity<DataResponse<MeetInfoListGetResponse>> getMeets(
            @RequestParam(value = "category", required = false) MeetCategory category,
            @RequestParam(value = "isLocal", required = false) Boolean isLocal,
            @RequestParam(value = "title", required = false) @Size(min = 2) String title,
            @Parameter(hidden = true) @AuthenticationPrincipal WithMeAppPrinciple principle
    ) {
        List<MeetDto> meetDtos = meetService.findAllMeets(category, isLocal, title, principle.getMemberId());

        MeetInfoListGetResponse response = MeetInfoListGetResponse.from(meetDtos);

        return new ResponseEntity<>(
                new DataResponse<>(response),
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "모임 기록 리스트 조회 API",
            description = "<p><code>meetStatus</code> 조건에 맞는 모임 기록 목록들을 반환합니다.</p>",
            security = @SecurityRequirement(name = "access-token")
    )
    @GetMapping("/meets/record")
    public ResponseEntity<DataResponse<MeetInfoListGetResponse>> getMeets(
            @RequestParam(value = "meetStatus") MeetStatus meetStatus,
            @Parameter(hidden = true) @AuthenticationPrincipal WithMeAppPrinciple principle
    ) {
        List<MeetDto> meetDtos = meetService.findAllMeetsRecords(MeetRecordSearch.of(meetStatus, principle.getMemberId()));

        MeetInfoListGetResponse response = MeetInfoListGetResponse.from(meetDtos);

        return new ResponseEntity<>(
                new DataResponse<>(response),
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "모임 해제 API",
            description = "<p><code>meetId</code>에 해당하는 모임을 해제 상태로 변경합니다.</p>",
            security = @SecurityRequirement(name = "access-token")
    )
    @PatchMapping("/meets/{meetId}")
    public ResponseEntity<DataResponse<MeetInfoGetResponse>> setMeetComplete(
            @PathVariable Long meetId,
            @Parameter(hidden = true) @AuthenticationPrincipal WithMeAppPrinciple principle
    ) {
        MeetDto meetDto = meetService.setMeetComplete(meetId, principle.getMemberId());

        MeetInfoGetResponse response = MeetInfoGetResponse.from(meetDto);

        return new ResponseEntity<>(
                new DataResponse<>(response),
                HttpStatus.OK
        );
    }
}