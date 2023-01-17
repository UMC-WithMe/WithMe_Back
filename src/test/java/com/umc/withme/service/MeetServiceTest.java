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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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
    void 모임_전체_조회_기능_테스트() {
        // given
        Address address1 = addressRepository.findBySidoAndSgg("서울특별시", "종로구").get();
        Address address2 = addressRepository.findBySidoAndSgg("서울특별시", "강남구").get();
        Address address3 = addressRepository.findBySidoAndSgg("부산광역시", "해운대구").get();

        Member member1 = createMember("1111@naver.com", "010-1111-1111", Gender.FEMALE, address1);
        Member member2 = createMember("2222@naver.com", "010-2222-2222", Gender.FEMALE, address2);
        Member member3 = createMember("3333@naver.com", "010-3333-3333", Gender.MALE, address3);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        Meet meet1 = createMeet(member1, "title1", "www.1111.com", "content1");
        Meet meet2 = createMeet(member2, "title2", "www.2222.com", "content2");
        Meet meet3 = createMeet(member3, "title3", "www.3333.com", "content3");
        meetService.createMeet(meet1);
        meetService.createMeet(meet2);
        meetService.createMeet(meet3);

        // when
        List<MeetDto> meetDtos = meetService.findMeets();

        // then
        for (MeetDto meetDto : meetDtos) {
            System.out.println("meetDto = " + meetDto);
        }
        assertThat(meetDtos.size()).isEqualTo(3);
    }

    private static Meet createMeet(Member member1, String title1, String link, String content1) {
        return Meet.builder()
                .leader(member1)
                .category(MeetCategory.EXERCISE)
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