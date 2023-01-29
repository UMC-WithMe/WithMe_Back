package com.umc.withme.controller;

import com.umc.withme.dto.common.DataResponse;
import com.umc.withme.dto.meet.*;
import com.umc.withme.security.WithMeAppPrinciple;
import com.umc.withme.service.MeetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    })
    @PostMapping("/meets")
    public ResponseEntity<DataResponse<MeetCreateResponse>> createMeet(
            @RequestBody MeetCreateRequest meetCreateRequest,
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
     * 모임 리스트 조회 API
     * 조건에 해당하는 모임 모집글 목록들을 조회한다.
     *
     * @param category 조회할 모임의 카테고리
     * @param sido     조회할 모임의 동네 시/도 주소
     * @param sgg      조회할 모임의 동네 시/군/구 주소
     * @param title    조회할 모임의 제목
     * @return 조건에 해당하는 모임 DTO 리스트를 반환한다.
     */
    @Operation(
            summary = "모임 리스트 조회 API",
            description = "<p><code>category</code>, <code>sido</code>, <code>sgg</code>, " +
                    "<code>title</code> 조건에 맞는 모임 모집글 목록들을 반환합니다.</p>",
            security = @SecurityRequirement(name = "access-token")
    )
    @GetMapping("/meets")
    public ResponseEntity<DataResponse<MeetInfoListGetResponse>> getMeets(
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "sido", required = false) String sido,
            @RequestParam(value = "sgg", required = false) String sgg,
            @RequestParam(value = "title", required = false) String title
    ) {
        System.out.println("category = " + category);
        System.out.println("sido = " + sido);
        System.out.println("sgg = " + sgg);
        System.out.println("title = " + title);
        List<MeetDto> meetDtos = meetService.findAll(MeetSearch.of(category, sido, sgg, title));
        for (MeetDto meetDto : meetDtos) {
            System.out.println("meetDto = " + meetDto);
        }

        MeetInfoListGetResponse response = MeetInfoListGetResponse.from(meetDtos);

        return new ResponseEntity<>(
                new DataResponse<>(response),
                HttpStatus.OK
        );
    }
}