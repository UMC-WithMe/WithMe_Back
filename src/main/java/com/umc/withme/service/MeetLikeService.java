package com.umc.withme.service;

import com.umc.withme.domain.Meet;
import com.umc.withme.domain.MeetLike;
import com.umc.withme.domain.Member;
import com.umc.withme.exception.meet.MeetIdNotFoundException;
import com.umc.withme.exception.member.MemberIdNotFoundException;
import com.umc.withme.repository.MeetLikeRepository;
import com.umc.withme.repository.MeetRepository;
import com.umc.withme.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MeetLikeService {

    private final MeetLikeRepository meetLikeRepository;
    private final MemberRepository memberRepository;
    private final MeetRepository meetRepository;

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
}
