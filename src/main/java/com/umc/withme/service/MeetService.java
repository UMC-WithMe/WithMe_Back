package com.umc.withme.service;

import com.umc.withme.domain.Meet;
import com.umc.withme.dto.Meet.MeetDto;
import com.umc.withme.repository.MeetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MeetService {

    private final MeetRepository meetRepository;

    /**
     * 새로운 Meet 엔티티를 입력받아 repository에 저장한다.
     * @param 저장할 Meet Entity
     */
    @Transactional
    public void createMeet(Meet meet){
        meetRepository.save(meet);
    }

    /**
     * Meet 목록들을 전체 조회한다.
     * @return 조회된 Meet 목록들의 리스트
     */
    public List<MeetDto> findMeets(){
        List<Meet> meets = meetRepository.findAll();
        List<MeetDto> result = meets.stream()
                .map(m -> MeetDto.from(m))
                .collect(Collectors.toList());
        return result;
    }
}


