package com.umc.withme.dto;

import com.umc.withme.domain.ImageFile;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ImageFileDto {

    private Long id;
    private String fileName;
    private String storedFileName;
    private String url;

    public static ImageFileDto of(Long id, String fileName, String storedFileName, String url) {
        return new ImageFileDto(id, fileName, storedFileName, url);
    }

    public static ImageFileDto from(ImageFile imageFile) {
        return of(
                imageFile.getId(),
                imageFile.getFileName(),
                imageFile.getStoredFileName(),
                imageFile.getUrl()
        );
    }
}
