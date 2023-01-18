package com.umc.withme.service;

import com.umc.withme.domain.Address;
import com.umc.withme.domain.Meet;
import com.umc.withme.domain.Member;
import com.umc.withme.domain.constant.Gender;
import com.umc.withme.domain.constant.MeetCategory;
import com.umc.withme.dto.Meet.MeetDto;
import com.umc.withme.repository.AddressRepository;
import com.umc.withme.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

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

    @Test
    public void 모임_목록_1개_조회() throws Exception{
        //given
        Address address = addressRepository.findBySidoAndSgg("서울특별시", "종로구").get();
        Member member = createMember("AAAA@naver.com", "010-AAAA-AAAA", Gender.FEMALE, address);
        memberRepository.save(member);
        Meet saveMeet = createMeet(member,MeetCategory.HOBBY,  "titleA", "www.AAAA.com", "contentA");
        meetService.createMeet(saveMeet);

        //when
        MeetDto findMeetDto = meetService.findMeetById(saveMeet.getId());

        //then
        if(findMeetDto == null){
            fail("모임 DTO가 조회되어야 한다.");
        }else{
            MeetDto saveMeetDto = MeetDto.from(saveMeet);
            assertThat(findMeetDto.getLeader().getNickName()).isEqualTo(saveMeetDto.getLeader().getNickName());
            assertThat(findMeetDto.getTitle()).isEqualTo(saveMeetDto.getTitle());
            assertThat(findMeetDto.getLink()).isEqualTo(saveMeetDto.getLink());
            assertThat(findMeetDto.getContent()).isEqualTo(saveMeetDto.getContent());
        }
    }


    @Test
    void 모임_전체_조회_기능_테스트() {
        // given

        // when
        List<MeetDto> meetDtos = meetService.findMeets();

        // then
        for (MeetDto meetDto : meetDtos) {
            System.out.println("meetDto = " + meetDto);
        }
        assertThat(meetDtos.size()).isEqualTo(3);
    }

    @Test
    public void 제목_으로_모임_리스트_조회() throws Exception{
        //given
        
        //when
        List<MeetDto> findMeetDtos = meetService.findMeetsByTitle("titleA");

        //then
        assertThat(findMeetDtos.size()).isEqualTo(2);
        assertThat(findMeetDtos.get(0).getContent()).isEqualTo("content1");
        assertThat(findMeetDtos.get(1).getContent()).isEqualTo("content3");
    }

    @Test
    public void 카테고리로_모임_리스트_조회() throws Exception{
        //given

        //when
        List<MeetDto> findMeetDtos = meetService.findMeetsByCategory("EXERCISE");

        //then
        assertThat(findMeetDtos.size()).isEqualTo(2);
        assertThat(findMeetDtos.get(0).getMeetCategory()).isEqualTo(MeetCategory.EXERCISE);
        assertThat(findMeetDtos.get(1).getMeetCategory()).isEqualTo(MeetCategory.EXERCISE);
        assertThat(findMeetDtos.get(0).getContent()).isEqualTo("content1");
        assertThat(findMeetDtos.get(1).getContent()).isEqualTo("content2");
    }

    @BeforeEach
    public void setUp(){
        Address address1 = addressRepository.findBySidoAndSgg("서울특별시", "종로구").get();
        Address address2 = addressRepository.findBySidoAndSgg("서울특별시", "강남구").get();
        Address address3 = addressRepository.findBySidoAndSgg("부산광역시", "해운대구").get();

        Member member1 = createMember("1111@naver.com", "010-1111-1111", Gender.FEMALE, address1);
        Member member2 = createMember("2222@naver.com", "010-2222-2222", Gender.FEMALE, address2);
        Member member3 = createMember("3333@naver.com", "010-3333-3333", Gender.MALE, address3);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        Meet meet1 = createMeet(member1, MeetCategory.EXERCISE, "titleA", "www.1111.com", "content1");
        Meet meet2 = createMeet(member2, MeetCategory.EXERCISE, "titleB", "www.2222.com", "content2");
        Meet meet3 = createMeet(member3, MeetCategory.HOBBY, "titleA", "www.3333.com", "content3");
        meetService.createMeet(meet1);
        meetService.createMeet(meet2);
        meetService.createMeet(meet3);
    }

    private static Meet createMeet(Member member1, MeetCategory category, String title1, String link, String content1) {
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

    private static Member createMember(String email, String phoneNumber, Gender gender, Address address) {

        return Member.builder()
                .address(address)
                .email(email)
                .phoneNumber(phoneNumber)
                .birth(LocalDate.now())
                .gender(gender)
                .build();
    }
}