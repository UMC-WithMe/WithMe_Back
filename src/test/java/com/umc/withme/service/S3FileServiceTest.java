package com.umc.withme.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.umc.withme.domain.ImageFile;
import com.umc.withme.repository.ImageFileRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@DisplayName("[Service] S3 File Upload")
@ExtendWith(MockitoExtension.class)
class S3FileServiceTest {

    @InjectMocks
    private S3FileService sut;

    @Mock
    private ImageFileRepository imageFileRepository;
    @Mock
    private AmazonS3Client s3Client;

    @Test
    void multipartFile이_주어지면_ImageFile_Entity를_생성하여_저장한_후_반환한다() {
        // given
        given(s3Client.putObject(any(PutObjectRequest.class))).willReturn(null);
        given(imageFileRepository.save(any(ImageFile.class))).willReturn(createImageFileWithId());

        // when
        sut.saveFile(
                new MockMultipartFile(
                        "file",
                        "test.txt",
                        MediaType.TEXT_PLAIN_VALUE,
                        "test".getBytes()
                )
        );

        // then
        then(imageFileRepository).should().save(any(ImageFile.class));
    }

    private ImageFile createImageFile() {
        return ImageFile.builder()
                .fileName("test")
                .storedFileName("test")
                .url("url")
                .build();
    }

    private ImageFile createImageFileWithId() {
        ImageFile imageFile = createImageFile();
        ReflectionTestUtils.setField(imageFile, "id", 1L);
        return imageFile;
    }
}