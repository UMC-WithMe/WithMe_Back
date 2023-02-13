package com.umc.withme.repository;

import com.umc.withme.domain.ImageFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ImageFileRepository extends JpaRepository<ImageFile, Long> {

    @Query("select count(imgfile.id) > 0 from ImageFile imgfile " +
            "where imgfile.fileName = 'default-profile-image' " +
            "and imgfile.storedFileName = 'default-profile-image'")
    boolean existsDefaultMemberProfileImage();

    @Query("select imgfile from ImageFile imgfile " +
            "where imgfile.fileName = 'default-profile-image' " +
            "and imgfile.storedFileName = 'default-profile-image'")
    ImageFile getDefaultMemberProfileImage();
}
