package com.umc.withme.controller;

import com.umc.withme.domain.Address;
import com.umc.withme.dto.common.DataResponse;
import com.umc.withme.dto.meet.MeetAddressDto;
import com.umc.withme.dto.meet.MeetCreateRequest;
import com.umc.withme.dto.meet.MeetCreateResponse;
import com.umc.withme.dto.meet.MeetDto;
import com.umc.withme.service.MeetService;
import com.umc.withme.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Validated
public class MeetController {

    private final MeetService meetService;
    private final MemberService memberService;

    @PostMapping("/meet")
    public ResponseEntity<DataResponse> createMeet(@Validated @RequestBody MeetCreateRequest meetCreateRequest){
        List<Address> addresses = meetCreateRequest.getAddresses()
                .stream().map(MeetAddressDto::toEntity)
                .collect(Collectors.toList());
        MeetDto meetDto = MeetDto.of(memberService.getMemberInfo(meetCreateRequest.getLeaderName()).toEntity(),
                meetCreateRequest.getMeetCategory(), meetCreateRequest.getRecruitStatus(),
                meetCreateRequest.getMeetStatus(), meetCreateRequest.getTitle(),
                meetCreateRequest.getLink(), meetCreateRequest.getContent(),
                meetCreateRequest.getMinPeople(), meetCreateRequest.getMaxPeople(),
                meetCreateRequest.getStartDate(), meetCreateRequest.getEndDate(),
                addresses);
//        System.out.println("meetDto.getStartDate() = " + meetDto.getStartDate());
//        System.out.println("meetDto.getEndDate() = " + meetDto.getEndDate());
        MeetDto savedMeetDto = meetService.createMeet(meetDto, meetCreateRequest.getAddresses());
//        System.out.println("savedMeetDto.getStartDate() = " + savedMeetDto.getStartDate());
//        System.out.println("savedMeetDto.getEndDate() = " + savedMeetDto.getEndDate());

        MeetCreateResponse response = MeetCreateResponse.from(savedMeetDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new DataResponse(response));
    }
}
