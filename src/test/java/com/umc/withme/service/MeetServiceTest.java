package com.umc.withme.service;

import com.umc.withme.domain.Address;
import com.umc.withme.domain.Meet;
import com.umc.withme.domain.Member;
import com.umc.withme.domain.constant.Gender;
import com.umc.withme.domain.constant.MeetCategory;
import com.umc.withme.dto.meet.MeetDto;
import com.umc.withme.repository.AddressRepository;
import com.umc.withme.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Meet - Service Layer Test")
@SpringBootTest
@Transactional
class MeetServiceTest {

    @Autowired
    AddressRepository addressRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    MeetService meetService;

    private static Meet getMeet(Member member1, MeetCategory category, String title1, String link, String content1) {
        return Meet.builder()
                .member(member1)
                .category(category)
                .title(title1)
                .minPeople(3)
                .maxPeople(5)
                .link(link)
                .content(content1)
                .build();
    }

    private static Member getMember(String email, String password, Integer ageRange, Gender gender) {

        return Member.builder()
                .email(email)
                .password(password)
                .ageRange(ageRange)
                .gender(gender)
                .build();
    }

    @Test
    public void 모임_id로_1개_조회하기() throws Exception {
        //given
        Member member = getMember(
                "AAAA@naver.com",
                "11112342",
                20,
                Gender.FEMALE);
        memberRepository.save(member);

        Meet meet = getMeet(
                member,
                MeetCategory.STUDY,
                "titleA",
                "www.fdsafd.com",
                "contentA");

        MeetDto originMeetDto = MeetDto.from(meet,
                List.of(
                        addressRepository.findBySidoAndSgg("서울특별시", "노원구")
                                .orElseThrow(EntityNotFoundException::new),
                        addressRepository.findBySidoAndSgg("서울특별시", "은평구")
                                .orElseThrow(EntityNotFoundException::new)),
                member);

        Long meetId = meetService.createMeet(originMeetDto, member.getNickname());

        //when
        MeetDto findMeetDto = meetService.findById(meetId);

        //then
        assertThat(findMeetDto.getLeader().getId()).isEqualTo(originMeetDto.getLeader().getId());
        assertThat(findMeetDto.getAddresses().size()).isEqualTo(2);
        assertThat(findMeetDto.getTitle()).isEqualTo(originMeetDto.getTitle());
        assertThat(findMeetDto.getLink()).isEqualTo(originMeetDto.getLink());
        assertThat(findMeetDto.getContent()).isEqualTo(originMeetDto.getContent());
        assertThat(findMeetDto.getMeetCategory()).isEqualTo(originMeetDto.getMeetCategory());
    }

    @BeforeEach
    public void setUp() {
        Address address1 = addressRepository.findBySidoAndSgg("서울특별시", "강남구").get();
        Address address2 = addressRepository.findBySidoAndSgg("서울특별시", "강남구").get();
        Address address3 = addressRepository.findBySidoAndSgg("부산광역시", "해운대구").get();

        Member member1 = getMember(
                "1111@naver.com",
                "1111",
                20,
                Gender.FEMALE);
        Member member2 = getMember(
                "2222@naver.com",
                "2222",
                20,
                Gender.FEMALE);
        Member member3 = getMember(
                "3333@naver.com",
                "3333",
                20,
                Gender.FEMALE);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        Meet meet1 = getMeet(member1, MeetCategory.EXERCISE, "titleA", "www.1111.com", "content1");
        Meet meet2 = getMeet(member2, MeetCategory.EXERCISE, "titleB", "www.2222.com", "content2");
        Meet meet3 = getMeet(member3, MeetCategory.HOBBY, "titleA", "www.3333.com", "content3");
        meetService.createMeet(MeetDto.from(meet1, List.of(address1), member1), member1.getNickname());
        meetService.createMeet(MeetDto.from(meet2, List.of(address2), member2), member2.getNickname());
        meetService.createMeet(MeetDto.from(meet3, List.of(address1, address3), member3), member3.getNickname());
    }
}