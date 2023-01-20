package com.umc.withme.controller;

import com.umc.withme.dto.common.DataResponse;
import com.umc.withme.dto.meet.MeetCreateRequest;
import com.umc.withme.dto.meet.MeetCreateResponse;
import com.umc.withme.dto.meet.MeetDto;
import com.umc.withme.service.MeetService;
import com.umc.withme.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<DataResponse<MeetCreateResponse>> createMeet(@Valid @RequestBody MeetCreateRequest meetCreateRequest){

        // 이거 toDto로 해서 한줄로 줄이기
        MeetDto meetDto = MeetDto.of(memberService.getMemberInfo(meetCreateRequest.getLeaderName()).toEntity(),
                meetCreateRequest.getMeetCategory(),
                meetCreateRequest.getTitle(), meetCreateRequest.getLink(), meetCreateRequest.getContent(),
                meetCreateRequest.getMinPeople(), meetCreateRequest.getMaxPeople(),
                meetCreateRequest.getAddresses());


        Long meetId = meetService.createMeet(meetDto);

        MeetCreateResponse response = MeetCreateResponse.of(meetId);

        return new ResponseEntity<>(
                new DataResponse<>(response),
                HttpStatus.OK
        );
    }
}
