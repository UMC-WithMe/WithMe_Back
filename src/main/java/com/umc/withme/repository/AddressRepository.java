package com.umc.withme.repository;

import com.umc.withme.domain.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {

    /**
     * <p>
     * 시/도, 시/군/구 정보를 전달받아 일치하는 Address entity를 조회한다.
     *
     * @param sido  시/도
     * @param sgg   시/군/구
     * @return {@link Address}  조회한 address entity
     */
    Optional<Address> findBySidoAndSgg(String sido, String sgg);
}
