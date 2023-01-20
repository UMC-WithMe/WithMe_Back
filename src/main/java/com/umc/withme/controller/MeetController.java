package com.umc.withme.controller;

import com.umc.withme.dto.common.DataResponse;
import com.umc.withme.dto.meet.MeetCreateRequest;
import com.umc.withme.dto.meet.MeetCreateResponse;
import com.umc.withme.dto.meet.MeetDto;
import com.umc.withme.service.MeetService;
import com.umc.withme.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MeetController {

    private final MeetService meetService;
    private final MemberService memberService;

    /**
     * 모임글 생성 API
     * @param meetCreateRequest
     * @return 생성된 모임글 id를 data 에 담아서 반환한다.
     */
    @PostMapping("/meet")
    public ResponseEntity<DataResponse<MeetCreateResponse>> createMeet(
            @Valid @RequestBody MeetCreateRequest meetCreateRequest,
            @RequestParam Long memberId // TODO : 인증기능 구현되면 로그인 사용자 정보 가져오는 걸로 변경
    ){

        Long meetId = meetService.createMeet(meetCreateRequest.toDto(), memberId);

        MeetCreateResponse response = MeetCreateResponse.of(meetId);

        return new ResponseEntity<>(
                new DataResponse<>(response),
                HttpStatus.OK
        );
    }
}
