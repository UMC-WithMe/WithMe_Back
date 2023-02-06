package com.umc.withme.service;

import com.umc.withme.domain.Meet;
import com.umc.withme.domain.MeetLike;
import com.umc.withme.domain.Member;
import com.umc.withme.dto.meet.MeetDto;
import com.umc.withme.dto.meet.MeetShortInfoResponse;
import com.umc.withme.dto.member.MemberDto;
import com.umc.withme.exception.meet.MeetIdNotFoundException;
import com.umc.withme.exception.member.MemberIdNotFoundException;
import com.umc.withme.repository.MeetLikeRepository;
import com.umc.withme.repository.MeetRepository;
import com.umc.withme.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MeetLikeService {

    private final MeetLikeRepository meetLikeRepository;
    private final MemberRepository memberRepository;
    private final MeetRepository meetRepository;

    /**
     * DB에 찜정보(회원, 모집글)를 새로 저장
     *
     * @param memberId
     * @param meetId
     */

    @Transactional
    public void createMeetLike(Long memberId, Long meetId) {

        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberIdNotFoundException(memberId));
        Meet meet = meetRepository.findById(meetId).orElseThrow(() -> new MeetIdNotFoundException(meetId));

        MeetLike meetLike = MeetLike.builder()
                .member(member)
                .meet(meet)
                .build();

        meetLikeRepository.save(meetLike);
    }

    /**
     * 전달받은 찜목록 DB에서 삭제
     *
     * @param meetLikeIdList
     */
    public void deleteMeetLike(List<Long> meetLikeIdList){

        for (Long meetLikeId: meetLikeIdList) {
            meetLikeRepository.deleteById(meetLikeId);
        }
    }

    /**
     * DB에서 특정 회원의 찜 목록 가져오기
     *
     * @param memberId
     */
    public List<MeetShortInfoResponse> findMeetLikeList(Long memberId){

        List<MeetShortInfoResponse> meetShortInfoResponseList = new ArrayList<>();
        Member member = memberRepository.findById(memberId).orElseThrow(()-> new MemberIdNotFoundException(memberId));

        List<MeetLike> meetLikeList = meetLikeRepository.findAllByMember_Id(memberId);
        for (MeetLike meetLike: meetLikeList) {
            Meet meet = meetLike.getMeet();

            Long meetLikeCount = meetLikeRepository.countByMeet_Id(meet.getId());

            meetShortInfoResponseList.add(MeetShortInfoResponse.of(MeetDto.from(meet), MemberDto.from(member), meetLikeCount));
        }

        return meetShortInfoResponseList;
    }
}
