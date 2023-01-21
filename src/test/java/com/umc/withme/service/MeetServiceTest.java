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
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Meet - Service Layer Test")
@SpringBootTest
@Transactional
//@Disabled("추후에 작성할 테스트입니다.")
class MeetServiceTest {

    @Autowired
    AddressRepository addressRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    MeetService meetService;

    private static Meet getMeet(Member member1, MeetCategory category, String title1, String link, String content1) {
        return Meet.builder()
                .leader(member1)
                .category(category)
                .title(title1)
                .minPeople(3)
                .maxPeople(5)
                .link(link)
                .content(content1)
                .build();
    }

    private static Member getMember(String email, String phoneNumber, Gender gender, Address address) {

        return Member.builder()
                .address(address)
                .email(email)
                .phoneNumber(phoneNumber)
                .birth(LocalDate.now())
                .gender(gender)
                .build();
    }

    @Test
    public void 모임_id로_1개_조회하기() throws Exception {
        //given
        Member member = getMember(
                "AAAA@naver.com",
                "010-1234-1234",
                Gender.FEMALE,
                addressRepository.findBySidoAndSgg("서울특별시", "노원구")
                        .orElseThrow(EntityNotFoundException::new));
        memberRepository.save(member);

        Meet meet = getMeet(
                member,
                MeetCategory.STUDY,
                "titleA",
                "www.fdsafd.com",
                "contentA");

        MeetDto originMeetDto = MeetDto.from(meet, List.of(
                addressRepository.findBySidoAndSgg("서울특별시", "노원구")
                        .orElseThrow(EntityNotFoundException::new),
                addressRepository.findBySidoAndSgg("서울특별시", "은평구")
                        .orElseThrow(EntityNotFoundException::new)
        ));

        Long meetId = meetService.createMeet(originMeetDto, member.getId());

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

        Member member1 = getMember("1111@naver.com", "010-1111-1111", Gender.FEMALE, address1);
        Member member2 = getMember("2222@naver.com", "010-2222-2222", Gender.FEMALE, address2);
        Member member3 = getMember("3333@naver.com", "010-3333-3333", Gender.MALE, address3);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        Meet meet1 = getMeet(member1, MeetCategory.EXERCISE, "titleA", "www.1111.com", "content1");
        Meet meet2 = getMeet(member2, MeetCategory.EXERCISE, "titleB", "www.2222.com", "content2");
        Meet meet3 = getMeet(member3, MeetCategory.HOBBY, "titleA", "www.3333.com", "content3");
        meetService.createMeet(MeetDto.from(meet1, List.of(address1)), member1.getId());
        meetService.createMeet(MeetDto.from(meet2, List.of(address2)), member2.getId());
        meetService.createMeet(MeetDto.from(meet3, List.of(address1, address3)), member3.getId());
    }
}