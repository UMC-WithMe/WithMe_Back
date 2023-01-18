package com.umc.withme.service;

import com.umc.withme.domain.Address;
import com.umc.withme.domain.Meet;
import com.umc.withme.domain.MeetAddress;
import com.umc.withme.domain.constant.MeetCategory;
import com.umc.withme.dto.Meet.MeetDto;
import com.umc.withme.repository.AddressRepository;
import com.umc.withme.repository.MeetAddressRepository;
import com.umc.withme.repository.MeetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MeetService {

    private final MeetRepository meetRepository;
    private final MeetAddressRepository meetAddressRepository;
    private final AddressRepository addressRepository;

    /**
     * 새로운 Meet 엔티티와 Address를 입력받아 모임 repository에 저장한다.
     * 그리고 MeetAddress repository에 meet과 address를 연결해 저장한다.
     * @param Meet Entity와 address Entity
     */
    @Transactional
    public void createMeet(Meet meet, Address address){
        meetRepository.save(meet);
        meetAddressRepository.save(new MeetAddress(meet, address));
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

    /**
     * id로 모임을 조회해서 DTO로 변환하여 반환한다.
     * @param 조회할 모임의 id
     * @return 조회된 모임의 DTO
     */
    public MeetDto findMeetById(Long meetId){
        Optional<Meet> optionalMeet = meetRepository.findById(meetId);
        if(optionalMeet.isPresent()){
            Meet findMeet = optionalMeet.get();
            MeetDto meetDto = MeetDto.from(findMeet);
            return meetDto;
        }else return null;
    }

    /**
     * 제목으로 모임을 조회해서 해당 제목을 가진 모임 DTO들의 목록을 반환한다.
     * @param 검색할 제목
     * @return 해당 제목의 모임 DTO 리스트
     */
    public List<MeetDto> findMeetsByTitle(String title){
        List<Meet> meets = meetRepository.findMeetsByTitle(title);
        return meets.stream()
                .map(meet -> MeetDto.from(meet))
                .collect(Collectors.toList());
    }

    /**
     * 카테고리로 모임을 조회해서 해당 카테고리의 모임 DTO 목록을 반환한다.
     * @param 조회할 카테고리명
     * @return 해당 카테고리의 모임 DTO 리스트
     */
    public List<MeetDto> findMeetsByCategory(String category){
        List<Meet> meets = meetRepository.findMeetsByCategory(MeetCategory.valueOf(category));
        return meets.stream()
                .map(meet -> MeetDto.from(meet))
                .collect(Collectors.toList());
    }

    /**
     * 동네로 모임을 조회해서 해당 동네의 모임 DTO 목록을 반환한다.
     * @param 조회할 동네 address
     * @return 해당 동네의 모임 DTO 리스트
     */
    public List<MeetDto> findMeetsByAddress(Address address){
        List<MeetAddress> meetAddresses = meetAddressRepository.findAllByAddressId(address.getId());
        List<Meet> meets = meetAddresses.stream()
                .map(MeetAddress::getMeet)
                .collect(Collectors.toList());
        List<MeetDto> meetDtos = meets.stream()
                .map(meet -> MeetDto.from(meet))
                .collect(Collectors.toList());
        return meetDtos;
    }
}


