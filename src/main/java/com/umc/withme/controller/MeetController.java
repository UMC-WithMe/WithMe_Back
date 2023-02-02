package com.umc.withme.controller;

import com.umc.withme.domain.constant.MeetCategory;
import com.umc.withme.domain.constant.MeetStatus;
import com.umc.withme.dto.common.BaseResponse;
import com.umc.withme.dto.common.DataResponse;
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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "MeetController", description = "모임 API Controller 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MeetController {

    private final MeetService meetService;

    /**
     * 모임글 생성 API
     *
     * @param meetFormRequest 생성하려는 모임 모집글 데이터
     * @param principle
     * @return 생성된 모임글 id를 data 에 담아서 반환한다.
     */
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
     * 모임 모집글 수정 API
     *
     * @param meetId          수정하려는 모집글의 id
     * @param meetFormRequest 수정하려는 정보가 담긴 요청 DTO
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
            @ApiResponse(responseCode = "404", description = "2400: <code>meetId</code>에 해당하는 모임이 없는 경우", content = @Content)
    })
    @PutMapping("/meets/{meetId}")
    public ResponseEntity<DataResponse<MeetInfoGetResponse>> updateMeet(
            @PathVariable Long meetId,
            @Valid @RequestBody MeetFormRequest meetFormRequest,
            @Parameter(hidden = true) @AuthenticationPrincipal WithMeAppPrinciple principle) {
        MeetDto meetDto = meetService.updateById(meetId, principle.getMemberId(), meetFormRequest.toDto());

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
        meetService.deleteMeetById(meetId, principle.getMemberId());

        return new ResponseEntity<>(
                new BaseResponse(true),
                HttpStatus.OK
        );
    }

    /**
     * 모임 모집글 리스트 조회 API
     * 조건에 해당하는 모임 모집글 목록들을 조회한다.
     *
     * @param category 조회할 모임의 카테고리
     * @param sido     조회할 모임의 동네 시/도 주소
     * @param sgg      조회할 모임의 동네 시/군/구 주소
     * @param title    조회할 모임의 제목
     * @return 조건에 해당하는 모임 DTO 리스트를 반환한다.
     */
    @Operation(
            summary = "모임 모집글 리스트 조회 API",
            description = "<p><code>category</code>, <code>sido</code>, <code>sgg</code>, " +
                    "<code>title</code> 조건에 맞는 모임 모집글 목록들을 반환합니다.</p>",
            security = @SecurityRequirement(name = "access-token")
    )
    @GetMapping("/meets")
    public ResponseEntity<DataResponse<MeetInfoListGetResponse>> getMeets(
            @RequestParam(value = "category", required = false) MeetCategory category,
            @RequestParam(value = "sido", required = false) String sido,
            @RequestParam(value = "sgg", required = false) String sgg,
            @RequestParam(value = "title", required = false) String title
    ) {
        List<MeetDto> meetDtos = meetService.findAllMeets(MeetSearch.of(category, sido, sgg, title));

        MeetInfoListGetResponse response = MeetInfoListGetResponse.from(meetDtos);

        return new ResponseEntity<>(
                new DataResponse<>(response),
                HttpStatus.OK
        );
    }

    /**
     * 모임 기록 목록 조회 API
     * 현재 모임 기록 및 지난 모임 기록 화면에서 사용된다.
     *
     * @param meetStatus 조회할 모임 기록 진행 상태 조건.
     *                   현재 모임 기록 : PROGRESS , 지난 모임 기록 : COMPLETE
     * @param principle
     * @return 조건에 해당하는 모임 DTO 리스트를 반환한다.
     */
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
        List<MeetDto> meetDtos = meetService.findAllMeetsRecords(MeetSearch.of(meetStatus, principle.getMemberId()));

        MeetInfoListGetResponse response = MeetInfoListGetResponse.from(meetDtos);

        return new ResponseEntity<>(
                new DataResponse<>(response),
                HttpStatus.OK
        );
    }
}